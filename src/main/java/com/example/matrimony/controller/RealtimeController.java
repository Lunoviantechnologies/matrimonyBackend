package com.example.matrimony.controller;

import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realtime")
public class RealtimeController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public RealtimeController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/online-users")
    public ResponseEntity<List<String>> onlineUsers() {
        return ResponseEntity.ok(chatService.onlineUsers());
    }

    @GetMapping("/recent-messages")
    public ResponseEntity<List<ChatMessage>> recentMessages() {
        return ResponseEntity.ok(chatService.recentMessages());
    }

    @PostMapping("/broadcast")
    public ResponseEntity<?> broadcast(@RequestBody ChatMessage m) {
        // Save message to DB
        chatService.addMessage(m);  
        // Send to subscribed clients
        messagingTemplate.convertAndSend("/topic/public", m);
        return ResponseEntity.ok().build();
    }
}
