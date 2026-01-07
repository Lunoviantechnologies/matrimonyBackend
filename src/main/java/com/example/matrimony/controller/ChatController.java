package com.example.matrimony.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;

    private final ProfileRepository profileRepository;

    public ChatController(ChatService chatService,
                          SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository messageRepository,
                          ProfileRepository profileRepository) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.profileRepository = profileRepository;
    }


   

	// ✅ This is REST API — works in Postman
    @GetMapping("/conversation/{senderId}/{receiverId}")
    public Page<ChatMessageDto> getConversation(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size
    ) {
        return chatService.getConversation(senderId, receiverId, page, size);
    }

    @GetMapping("/online")  // ✅ already working
    public List<String> onlineUsers() {
        return chatService.onlineUsers();
    }
    @PostMapping("/seen/{senderId}/{receiverId}")
    public void markSeen(@PathVariable Long senderId,
                         @PathVariable Long receiverId) {
        chatService.markSeen(senderId, receiverId);
    }

    // ✅ Also add this for REST send testing (optional)
    @PostMapping("/send/{senderId}/{receiverId}")
    public ChatMessageDto sendMessageHttp(
        @PathVariable Long senderId,
        @PathVariable Long receiverId,
        @RequestBody ChatMessageDto dto
    ){
        return chatService.sendMessage(senderId, receiverId, dto.getMessage());
    }
    
    @MessageMapping("/chat.send/{receiverId}")
    public void sendMessage(
            @DestinationVariable Long receiverId,
            @Payload ChatMessageDto dto,
            Principal principal
    ) {

        if (dto == null) {
            System.err.println("❌ ChatMessageDto payload is NULL");
            return; // prevents crash
        }

        Long senderId = Long.parseLong(principal.getName());

        ChatMessageDto saved =
                chatService.sendMessage(senderId, receiverId, dto.getMessage());

        if (saved == null) {
            System.err.println("❌ Saved message is NULL");
            return;
        }

        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                saved
        );
    }
    
    
    
    
    
    //clear chat 
    @PostMapping("/clear/{senderId}/{receiverId}")
    public String clearChat(
            @PathVariable Long senderId,
            @PathVariable Long receiverId
    ) {
        chatService.clearChat(senderId, receiverId);
        return "Chat cleared for user " + senderId;
    }


    
}