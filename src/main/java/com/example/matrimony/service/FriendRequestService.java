package com.example.matrimony.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.FriendDTO;
import com.example.matrimony.dto.FriendRequestDTO;
import com.example.matrimony.dto.NotificationDto;
import com.example.matrimony.entity.FriendRequest;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.ProfileRepository;

@Service
@Transactional
public class FriendRequestService {


	private final NotificationService notificationService;


    private final FriendRequestRepository requestRepository;
    private final ProfileRepository profileRepository;
    
    public FriendRequestService(FriendRequestRepository requestRepository,
                                ProfileRepository profileRepository,
                                NotificationService notificationService) {
        this.requestRepository = requestRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }
    

    public FriendRequest sendRequest(Long senderId, Long receiverId) {
        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequest.Status.PENDING);

        // Save first to get ID
        FriendRequest saved = requestRepository.save(friendRequest);

      

        return saved;
    }

    
    
    
    // RECEIVED API
    public List<FriendRequestDTO> getReceivedRequests(Long receiverId) {
        List<FriendRequest> requests = requestRepository.findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING);

        return requests.stream()
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getReceiver().getFirstName()+""+req.getReceiver().getLastName(),
                        req.getSender().getEmailId(),
                        req.getStatus()
                ))
                .toList();
    }
   
 
    
    public FriendRequest respondToRequest(Long requestId, boolean accept) {
        // 1️⃣ Fetch the request
        FriendRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // 2️⃣ Check status
        if (request.getStatus() != FriendRequest.Status.PENDING)
            throw new RuntimeException("This request is already responded!");

        Profile sender = request.getSender();
        Profile receiver = request.getReceiver();

        // 3️⃣ Accept or Reject
        if (accept) {
            request.setStatus(FriendRequest.Status.ACCEPTED);

            if (!sender.getFriends().contains(receiver)) {
                sender.getFriends().add(receiver);
                profileRepository.save(sender);
            }

            if (!receiver.getFriends().contains(sender)) {
                receiver.getFriends().add(sender);
                profileRepository.save(receiver);
            }
        } else {
            request.setStatus(FriendRequest.Status.REJECTED);
        }

        // 4️⃣ Notification to sender
        NotificationDto notif = new NotificationDto();
        notif.setSenderId(receiver.getId());
        notif.setReceiverId(sender.getId());
        notif.setType(accept ? "FRIEND_REQUEST_ACCEPTED" : "FRIEND_REQUEST_REJECTED");
        notif.setMessage(receiver.getFirstName() + " " + receiver.getLastName() +
                (accept ? " accepted " : " rejected ") + "your friend request");
        notif.setData(Map.of("requestId", requestId, "accepted", accept));

        notificationService.sendToUserAndSave(notif);

       
        return requestRepository.save(request);
    }

    
 

    
    public List<FriendDTO> getSentBy(Long senderId) {
        List<FriendRequest> requests = requestRepository.findBySender_Id(senderId);

        return requests.stream()
            .map(req -> new FriendDTO(
                req.getId(),    
                req.getSender().getId(),
                req.getReceiver().getId(),
                req.getReceiver().getFirstName() + " " + req.getReceiver().getLastName(),           
                req.getStatus().name()
            ))
            .toList();
    }

 
    public String deleteSentRequest(Long requestId) {
        if (!requestRepository.existsById(requestId)) {
            return "Friend request not found!";
        }
        requestRepository.deleteById(requestId);
        return "Friend request deleted successfully!";
    }
    public List<FriendRequestDTO> getAcceptedRequests() {
        return requestRepository.findByStatus(FriendRequest.Status.ACCEPTED).stream()  // ✅ enum
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getSender().getEmailId(),
                        req.getReceiver().getFirstName()+""+req.getReceiver().getLastName(),
                        req.getStatus()))
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> getRejectedRequests() {
        return requestRepository.findByStatus(FriendRequest.Status.REJECTED).stream() // ✅ enum
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getReceiver().getFirstName()+""+req.getReceiver().getLastName(),
                        req.getSender().getEmailId(),
                        req.getStatus()))
                .collect(Collectors.toList());
    }
    
    public List<FriendRequestDTO> getAcceptedReceived(Long userId) {
        return mapToDTO(requestRepository.findAcceptedReceived(userId));
    }

//    public List<FriendRequestDTO> getAcceptedSent(Long userId) {
//        return mapToDTO(requestRepository.findAcceptedSent(userId));
//    }
    public List<FriendRequestDTO> getAcceptedSent(Long userId) {
        // 1️⃣ Fetch the accepted friend requests
        List<FriendRequest> requests = requestRepository.findAcceptedSent(userId);
 
        // 3️⃣ Map to DTO and return
        return mapToDTO(requests);
    }


    private List<FriendRequestDTO> mapToDTO(List<FriendRequest> list) {
        return list.stream().map(fr -> {
            FriendRequestDTO dto = new FriendRequestDTO();
            dto.setRequestId(fr.getId());
            dto.setStatus(fr.getStatus());
            dto.setSenderId(fr.getSender().getId());
            dto.setReceiverId(fr.getReceiver().getId());
            dto.setSenderName(fr.getSender().getFirstName() + " " + fr.getSender().getLastName());
            dto.setReceiverName(fr.getReceiver().getFirstName() + " " + fr.getReceiver().getLastName());
            return dto;
        }).toList();
    }

    public List<FriendRequestDTO> getRejectedReceived(Long userId) {
        return mapToDTO(requestRepository.findRejectedReceived(userId));
    }

    public List<FriendRequestDTO> getRejectedSent(Long userId) {
        return mapToDTO(requestRepository.findRejectedSent(userId));
    }
    
    
    public List<FriendRequestDTO> getAllFriendRequests() {

        List<FriendRequestDTO> requests =
                requestRepository.findAllWithSenderAndReceiver()
                        .stream()
                        .map(fr -> new FriendRequestDTO(
                                fr.getId(),
                                fr.getSender().getId(),
                                fr.getReceiver().getId(),
                                fr.getSender().getFirstName() + " " + fr.getSender().getLastName(),
                                fr.getReceiver().getFirstName() + " " + fr.getReceiver().getLastName(),
                                fr.getSender().getEmailId(),
                                fr.getStatus()
                        ))
                        .toList();

        // 🔔 GIVE MESSAGE HERE (ONLY HERE)
        

        return requests;
    }

    
    
    
     
//    

  // Check if two users are friends
    public boolean areFriends(Long userId1, Long userId2) {
        Profile user1 = profileRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user1.getFriends().stream()
                .anyMatch(friend -> friend.getId().equals(userId2));
    }

    // Get pending requests received by a user
    public List<FriendRequest> getPendingRequests(Long receiverId) {
        return requestRepository.findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING);
    }

      
}