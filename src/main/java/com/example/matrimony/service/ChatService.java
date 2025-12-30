//package com.example.matrimony.service;
//
//import com.example.matrimony.dto.ChatMessageDto;
//import com.example.matrimony.entity.ChatMessage;
//import com.example.matrimony.repository.ChatMessageRepository;
//import com.example.matrimony.repository.ProfileRepository;
//import com.example.matrimony.entity.Profile;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@Transactional
//public class ChatService {
//
//    private final ChatMessageRepository messageRepository;
//    private final FriendRequestService friendService;
//    private final ProfileRepository profileRepo;
//
//    // Track online users via mobileNumber
//    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
//
//    public ChatService(ChatMessageRepository messageRepository,
//                       FriendRequestService friendService,
//                       ProfileRepository profileRepo) {
//        this.messageRepository = messageRepository;
//        this.friendService = friendService;
//        this.profileRepo = profileRepo;
//    }
// 
//
//    public void userConnected(String mobileNumber) {
//        onlineUsers.add(mobileNumber);
//    }
//
//    public void userDisconnected(String mobileNumber) {
//        onlineUsers.remove(mobileNumber);
//    }
//
//    public List<String> onlineUsers() {
//        return new ArrayList<>(onlineUsers);
//    }
//
//    
//    
//    public ChatMessageDto sendMessage(Long senderId, Long receiverId, String msg) {
//
//    	Profile sender = profileRepo.findById(senderId).orElseThrow();
//    	Profile receiver = profileRepo.findById(receiverId).orElseThrow();
//
//        // ✅ Convert to entity before saving
//        ChatMessage chat = new ChatMessage();
//        chat.setSender(sender);
//        chat.setReceiver(receiver);
//        chat.setMessage(msg);  // ✅ saving correct message taken from DTO
//        chat.setTimestamp(LocalDateTime.now());
//
//        messageRepository.save(chat);
//
//        // ✅ Convert back to DTO response
//        return new ChatMessageDto(
//            chat.getId(),
//            senderId,
//            receiverId,
//            chat.getMessage(),
//            chat.getTimestamp()
//        );
//    }
//
//
//    public Page<ChatMessageDto> getConversation(Long senderId, Long receiverId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<ChatMessage> conversation = messageRepository
//        	    .findBySender_IdAndReceiver_IdOrReceiver_IdAndSender_IdOrderByTimestampAsc(
//        	        senderId, receiverId, receiverId, senderId, pageable
//        	    );
//
//
//        // Convert Page<ChatMessage> → Page<ChatMessageDTO>
//        return conversation.map(c -> new ChatMessageDto(
//                c.getId(),
//                c.getSender().getId(),
//                c.getReceiver().getId(),
//                c.getMessage(),
//                c.getTimestamp()
//        ));
//    }
//
//
//    
//    public ChatMessage addMessage(ChatMessage message) {
//        message.setTimestamp(LocalDateTime.now());
//        return messageRepository.save(message);
//    }
//
//    
//   
// 
//    public List<ChatMessage> recentMessages() {
//        return messageRepository.findTop20ByOrderByTimestampDesc();
//    }
//}

package com.example.matrimony.service;

import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.dto.NotificationDto;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.ProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class ChatService {
	
	@Autowired
    private Notificationadminservice adminNotificationService;


    private final ChatMessageRepository messageRepository;
    private final ProfileRepository profileRepository;
    private final NotificationService notificationService;
    


    // ✅ Track online users by profile ID (or mobile number if you want)
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public ChatService(ChatMessageRepository messageRepository,
    		ProfileRepository profileRepository,
    		NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }

    // ✅ Save message to database and return DTO
//   public ChatMessageDto sendMessage(Long senderId, Long receiverId, String msg) {
//
//        if (msg == null || msg.isBlank()) {
//            throw new RuntimeException("Message cannot be empty");
//       }
//
//        Profile sender = profileRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("Sender Profile not found"));
//
//        Profile receiver = profileRepository.findById(receiverId)
//                .orElseThrow(() -> new RuntimeException("Receiver Profile not found"));
//
//        ChatMessage chat = new ChatMessage();
//        chat.setSender(sender);
//        chat.setReceiver(receiver);
//        chat.setMessage(msg);
//        chat.setTimestamp(LocalDateTime.now());
//
//        messageRepository.save(chat);
//
//        return new ChatMessageDto(
//                chat.getId(),
//                senderId,
//                receiverId,
//                chat.getMessage(),
//                chat.getTimestamp()
//        );
//    }
    
    // SEND MESSAGE
//    public ChatMessageDto sendMessage(Long senderId, Long receiverId, String message) {
//
//        Profile sender = profileRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("Sender not found"));
//
//        Profile receiver = profileRepository.findById(receiverId)
//                .orElseThrow(() -> new RuntimeException("Receiver not found"));
//
//        ChatMessage msg = new ChatMessage();
//        msg.setSender(sender);
//        msg.setReceiver(receiver);
//        msg.setMessage(message);
//
//        ChatMessage saved = messageRepository.save(msg);
//
//        // 🔔 Notification: New Chat Message
//        NotificationDto notification = new NotificationDto();
//        notification.setType("CHAT_MESSAGE");
//        notification.setMessage("New message from " + sender.getFirstName());
//        notification.setSenderId(senderId);
//        notification.setReceiverId(receiverId);
//        notification.setData(Map.of(
//                "chatId", saved.getId(),
//                "message", message,
//                "senderName", sender.getLastName()
//        ));
//
//        notificationService.sendToUserAndSave(notification);
//
//        return ChatMessageDto.fromEntity(saved);
//    }

    public ChatMessageDto sendMessage(Long senderId, Long receiverId, String message) {

        // 1️⃣ Fetch sender and receiver
        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 2️⃣ Save chat message
        ChatMessage msg = new ChatMessage();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setMessage(message);

        ChatMessage saved = messageRepository.save(msg);

        // 3️⃣ Create notification message text (same as admin-style)
        String userMessage = sender.getFirstName() + " " + sender.getLastName() +
                " sent you a new message: \"" + message + "\"";

        // 4️⃣ Notification for receiver (user)
        NotificationDto notification = new NotificationDto();
        notification.setType("CHAT_MESSAGE");
        notification.setMessage(userMessage);          // same style as admin message
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

        // 5️⃣ 🔔 Optional: Notify all admins that a new chat message was sent
        String adminMessage = sender.getFirstName() + " " + sender.getLastName() +
                " sent a message to " +
                receiver.getFirstName() + " " + receiver.getLastName();

        adminNotificationService.notifyAdmin(
                "CHAT_MESSAGE",
                adminMessage,
                Map.of(
                        "chatId", saved.getId(),
                        "senderId", senderId,
                        "receiverId", receiverId,
                        "message", message
                )
        );

        // 6️⃣ Return DTO
        return ChatMessageDto.fromEntity(saved);
    }



    // ✅ Fetch conversation (paginated)
    public Page<ChatMessageDto> getConversation(Long senderId, Long receiverId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ChatMessage> conversation = messageRepository
                .findBySender_IdAndReceiver_IdOrReceiver_IdAndSender_IdOrderByTimestampAsc(
                        senderId, receiverId, senderId, receiverId, pageable
                );

        return conversation.map(c -> new ChatMessageDto(
                c.getId(),
                c.getSender().getId(),
                c.getReceiver().getId(),
                c.getMessage(),
                c.getTimestamp()
        ));
    }

    // ✅ Store user online when connected
    public void userConnected(String id) {
        onlineUsers.add(id);
    }

    // ✅ Remove user when disconnected
    public void userDisconnected(String id) {
        onlineUsers.remove(id);
    }

    // ✅ Get online users list
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
}

