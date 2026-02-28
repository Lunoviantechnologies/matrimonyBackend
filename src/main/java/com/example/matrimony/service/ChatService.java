
package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.example.matrimony.exception.PremiumRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.ChatContactDto;
import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.dto.NotificationDto;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.ProfileRepository;

@Service
@Transactional
public class ChatService {
	
	private final BlockService blockService;  
    private final ChatMessageRepository messageRepository;
    private final ProfileRepository profileRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProfileService profileService;
    
    //  Track online users by profile ID (or mobile number if you want)
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public ChatService(ChatMessageRepository messageRepository,
    		ProfileRepository profileRepository,
    		NotificationService notificationService,
    		 BlockService blockService,
    		 ProfileService profileService,
    		 SimpMessagingTemplate messagingTemplate
    		 ) {
        this.messageRepository = messageRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
        this.blockService = blockService;
        this.messagingTemplate = messagingTemplate;
        this.profileService=profileService;
    }
 

    public ChatMessageDto sendMessage(Long senderId, Long receiverId, String message) {

    	  //  BLOCK CHECK (VERY FIRST LINE)
        if (blockService.isBlocked(senderId, receiverId)) {
            throw new RuntimeException("Messaging blocked");
        }
        // 1️ Fetch sender and receiver
        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
     // ⭐ FREE USER LOGIC
        if (!sender.isPremium()) {

        	long systemCount =
                    messageRepository.countSystemMessages(senderId, receiverId);

            // FIRST TIME clicking SEND → insert default messages
            if (systemCount == 0) {

                ChatMessage m1 = saveSystemMessage(
                        sender,
                        receiver,
                        "Hi, I came across your profile and found it interesting."
                );

                ChatMessage m2 = saveSystemMessage(
                        sender,
                        receiver,
                        "I would like to know more about you and your family. If interested let's connect."
                );

                sendSocket(m1, senderId, receiverId);
                sendSocket(m2, senderId, receiverId);

                return ChatMessageDto.fromEntity(m1);
            }
            // Already used free limit
            throw new PremiumRequiredException(
                    "Upgrade to premium to continue chatting."
            );
        }
        // 2️ Save chat message
        ChatMessage msg = new ChatMessage();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setMessage(message);
        msg.setTimestamp(LocalDateTime.now());
        msg.setSystem(false);

        ChatMessage saved = messageRepository.save(msg);

        // 3️ Create notification message text (same as admin-style)
        String userMessage = sender.getFirstName() + " " + sender.getLastName() +
                " sent you a new message: \"" + message + "\"";

        // 4️ Notification for receiver (user)
        NotificationDto notification = new NotificationDto();
        notification.setType("CHAT_MESSAGE");
        notification.setMessage(userMessage);        
        notification.setSenderId(senderId);
        notification.setReceiverId(receiverId);
        notification.setData(Map.of(
                "chatId", saved.getId(),
                "message", message,
                "senderId", senderId,
                "senderName", sender.getFirstName() + " " + sender.getLastName()
        ));

        // Send notification to user
        notificationService.sendToUserAndSave(notification);
        return ChatMessageDto.fromEntity(saved);
    }
    
   
    public void markSeen(Long senderId, Long receiverId) {
        int updated = messageRepository.markMessagesAsSeen(senderId, receiverId);
        if (updated > 0) {
            messagingTemplate.convertAndSendToUser(
                    senderId.toString(),
                    "/queue/seen",
                    receiverId
            );
        }
    }

    //  Fetch conversation (paginated)
    public Page<ChatMessageDto> getConversation(
            Long senderId,
            Long receiverId,
            int page,
            int size
    ) {

        //  fetch profiles
        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Page<ChatMessage> messages =
                messageRepository.findConversationForUser(
                        senderId,
                        receiverId,
                        pageable
                );

        return messages.map(c -> {
            ChatMessageDto dto = new ChatMessageDto();
            dto.setId(c.getId());
            dto.setSenderId(c.getSender().getId());
            dto.setReceiverId(c.getReceiver().getId());
            dto.setMessage(c.getMessage());
            dto.setTimestamp(c.getTimestamp());
            dto.setSeen(c.isSeen());
            dto.setSeenAt(c.getSeenAt());
            return dto;
        });
    }

    

    //  Store user online when connected
    public void userConnected(String id) {
        onlineUsers.add(id);
    }

    //  Remove user when disconnected
    public void userDisconnected(String id) {
        onlineUsers.remove(id);
    }

    //  Get online users list
    public List<String> onlineUsers() {
        return new ArrayList<>(onlineUsers);
    }

    public ChatMessage addMessage(ChatMessage message) {
      message.setTimestamp(LocalDateTime.now());
      return messageRepository.save(message);
  }
//
  
    
    public List<ChatMessage> recentMessages() {
     return messageRepository.findTop20ByOrderByTimestampDesc();
  }
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getSeenStatus(Long senderId, Long receiverId) {

        return messageRepository
                .getConversationWithSeenStatus(senderId, receiverId)
                .stream()
                .map(ChatMessageDto::fromEntity)
                .toList();
    }

    
    //clear chat
    @Transactional
    public void clearChat(Long senderId, Long receiverId) {

        // user clears messages where he is sender
        messageRepository.clearChatAsSender(senderId, receiverId);

        // user clears messages where he is receiver
        messageRepository.clearChatAsReceiver(senderId, receiverId);
    }
    
    @Transactional(readOnly = true)
    public Page<ChatContactDto> getChatContacts(
            Long myId,
            String search,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "lastMessageTime")
        );

        Page<Profile> contacts =
                profileRepository.findAcceptedChatContacts(
                        myId,
                        search == null ? "" : search,
                        pageable
                );

        return contacts.map(profile -> {

            // last message
        	List<ChatMessage> lastMsgList =messageRepository.findLastMessageBetweenUsers(
        	                myId,
        	                profile.getId(),
        	                PageRequest.of(0,1)
        	        );

        	ChatMessage last =lastMsgList.isEmpty() ? null : lastMsgList.get(0);;
        	String lastMessage = last != null ? last.getMessage() : null;
        	LocalDateTime lastTime = last != null ? last.getTimestamp() : null;

            boolean online =
                    onlineUsers.contains(profile.getId().toString());

            boolean blockedByMe =
                    blockService.isBlocked(myId, profile.getId());

            boolean blockedByOther =
                    blockService.isBlocked(profile.getId(), myId);

            Map<Integer, String> photoMap =
                    profileService.getPhotoMap(profile.getId());

            String photoUrl = photoMap.getOrDefault(0, null); // main photo index 0

            return new ChatContactDto(
                    profile.getId(),
                    profile.getFirstName() + " " + profile.getLastName(),
                    photoUrl,
                    lastMessage,
                    lastTime,
                    online,
                    blockedByMe,
                    blockedByOther
            );
        });
    }
    
    private ChatMessage saveSystemMessage(Profile sender, Profile receiver, String text) {
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(text);
        message.setTimestamp(LocalDateTime.now());
        message.setSystem(true);
        return messageRepository.save(message);
    }
    private void sendSocket(ChatMessage msg, Long senderId, Long receiverId) {

        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                ChatMessageDto.fromEntity(msg)
        );

        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                ChatMessageDto.fromEntity(msg)
        );
    }
}

