package com.example.matrimony.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.entity.ArchivedChatMessage;
import com.example.matrimony.service.ArchivedChatMessageService;

@RestController
@RequestMapping("/api/archived-chats")
@CrossOrigin
public class ArchivedChatMessageController {

    private final ArchivedChatMessageService service;

    public ArchivedChatMessageController(
            ArchivedChatMessageService service) {
        this.service = service;
    }

    // ✅ GET archived chats between sender & receiver
    @GetMapping("/between")
    public ResponseEntity<List<ArchivedChatMessage>> 
        getArchivedChatsBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        return ResponseEntity.ok(
            service.getArchivedChatsBetweenUsers(senderId, receiverId)
        );
    }

    // ✅ GET archived chats by deleted user
    @DeleteMapping("Get/{deletedUserId}")
    public ResponseEntity<List<ArchivedChatMessage>> 
        getArchivedChatsByDeletedUser(
            @PathVariable Long deletedUserId) {

        return ResponseEntity.ok(
            service.getArchivedChatsByDeletedUser(deletedUserId)
        );
    }
}
