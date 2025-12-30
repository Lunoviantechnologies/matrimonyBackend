package com.example.matrimony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.FriendDTO;
import com.example.matrimony.dto.FriendRequestDTO;
import com.example.matrimony.entity.FriendRequest;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.FriendRequestService;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @Autowired
    ProfileRepository profileRepository;
    public FriendRequestController(FriendRequestService service) {
        this.friendRequestService = service;
    }
    
    // Send friend request
//    @PostMapping("/send")
//    public FriendRequest sendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
//        return friendRequestService.sendRequest(senderId, receiverId);
//    }
    @PostMapping("/send/{senderId}/{receiverId}")
    public FriendRequest sendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return friendRequestService.sendRequest(senderId, receiverId);
    }
    
    // Get all requests received by a user
    @GetMapping("/received/{receiverId}")
    public List<FriendRequestDTO> getReceivedRequests(@PathVariable Long receiverId) {
        return friendRequestService.getReceivedRequests(receiverId);
    }

 
    @PostMapping("/respond/{requestId}")
    public FriendRequest respond(@PathVariable Long requestId, @RequestParam boolean accept) {
        return friendRequestService.respondToRequest(requestId, accept);
    }
    
    @GetMapping("/sent/{userId}")
    public List<FriendDTO> sent(@PathVariable Long userId) {
        return friendRequestService.getSentBy(userId);
    }
    
    @DeleteMapping("/sent/delete/{requestId}")
    public String deleteSent(@PathVariable Long requestId) {
        return friendRequestService.deleteSentRequest(requestId);
    }
    
    @GetMapping("/accepted/received/{userId}")

    public ResponseEntity<List<FriendRequestDTO>> getAcceptedReceived(@PathVariable Long userId) {

        return ResponseEntity.ok(friendRequestService.getAcceptedReceived(userId));

    }
  
    @GetMapping("/accepted/sent/{userId}")

    public ResponseEntity<List<FriendRequestDTO>> getAcceptedSent(@PathVariable Long userId) {

        return ResponseEntity.ok(friendRequestService.getAcceptedSent(userId));

    }
    @GetMapping("/rejected/received/{userId}")
    public ResponseEntity<List<FriendRequestDTO>> getRejectedReceived(@PathVariable Long userId) {
        return ResponseEntity.ok(friendRequestService.getRejectedReceived(userId));
    }     

    @GetMapping("/rejected/sent/{userId}")
    public ResponseEntity<List<FriendRequestDTO>> getRejectedSent(@PathVariable Long userId) {
        return ResponseEntity.ok(friendRequestService.getRejectedSent(userId));
    }

    
    
//    @GetMapping("/accepted")
//    public List<FriendRequestDTO> getAccepted() {
//        return friendRequestService.getAcceptedRequests();
//    }
//    @GetMapping("/accepted/{userId}")
//    public ResponseEntity<List<FriendRequest>> getAccepted(@PathVariable Long userId) {
//        List<FriendRequest> accepted = friendRequestService.getAcceptedFriends(userId);
//        return ResponseEntity.ok(accepted);
//    }
//
//    @GetMapping("/{userId}/accepted")
//    public List<FriendRequest> getAcceptedFriends(@PathVariable Long userId) {
//        return friendRequestService.getAcceptedFriends(userId);
//    }

//    // Get friends by custom status
//    @GetMapping("/{userId}/status/{status}")
//    public List<FriendRequest> getFriendsByStatus(@PathVariable Long userId,
//                                                  @PathVariable Status status) {
//        return friendRequestService.getFriendsByStatus(userId, status);
//    }
    // ✅ GET Rejected Friend Requests
    @GetMapping("/rejected")
    public List<FriendRequestDTO> getRejected() {
        return friendRequestService.getRejectedRequests();
    }
    
 // ================= GET ALL FRIEND REQUESTS =================
    @GetMapping("/all")
    public ResponseEntity<List<FriendRequestDTO>> getAllFriendRequests() {
        return ResponseEntity.ok(
                friendRequestService.getAllFriendRequests()
        );
    }


}

