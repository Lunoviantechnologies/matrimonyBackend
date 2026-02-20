package com.example.matrimony.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // ✅ SEND FRIEND REQUEST + NOTIFICATION
    public FriendRequest sendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("You cannot send request to yourself");
        }

        Profile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Profile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // ✅ Check already friends
        if (sender.getFriends().contains(receiver)) {
            throw new RuntimeException("Already friends");
        }

        // ✅ Prevent duplicate pending request
        boolean alreadyPending = requestRepository
                .existsBySender_IdAndReceiver_IdAndStatus(senderId, receiverId, FriendRequest.Status.PENDING);

        if (alreadyPending) {
            throw new RuntimeException("Friend request already sent and pending");
        }

        // ✅ Save request
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequest.Status.PENDING);

        FriendRequest saved = requestRepository.save(friendRequest);

        // 🔔 SEND NOTIFICATION TO RECEIVER (USER B)
        NotificationDto notif = new NotificationDto();
        notif.setSenderId(sender.getId());
        notif.setReceiverId(receiver.getId());
        notif.setType("FRIEND_REQUEST_RECEIVED");
        notif.setMessage(sender.getFirstName() + " " + sender.getLastName() + " sent you a friend request");
        notif.setData(Map.of(
                "requestId", saved.getId(),
                "status", saved.getStatus().name()
        ));

        notificationService.sendToUserAndSave(notif);

        return saved;
    }

    // ✅ RECEIVED REQUESTS
    public List<FriendRequestDTO> getReceivedRequests(Long receiverId) {

        List<FriendRequest> requests =
                requestRepository.findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING);

        return requests.stream()
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getReceiver().getFirstName() + " " + req.getReceiver().getLastName(),
                        req.getSender().getEmailId(),
                        req.getStatus()
                ))
                .toList();
    }

    // ✅ ACCEPT / REJECT REQUEST + NOTIFICATION
    public FriendRequest respondToRequest(Long requestId, boolean accept) {

        FriendRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new RuntimeException("This request is already responded!");
        }

        Profile sender = request.getSender();
        Profile receiver = request.getReceiver();

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

        FriendRequest updated = requestRepository.save(request);

        // 🔔 Notify sender about accept/reject
        NotificationDto notif = new NotificationDto();
        notif.setSenderId(receiver.getId());
        notif.setReceiverId(sender.getId());
        notif.setType(accept ? "FRIEND_REQUEST_ACCEPTED" : "FRIEND_REQUEST_REJECTED");
        notif.setMessage(receiver.getFirstName() + " " + receiver.getLastName() +
                (accept ? " accepted " : " rejected ") + "your friend request");
        notif.setData(Map.of(
                "requestId", requestId,
                "accepted", accept
        ));

        notificationService.sendToUserAndSave(notif);

        return updated;
    }

    // ✅ SENT REQUESTS (NO NOTIFICATION HERE)
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
        return requestRepository.findByStatus(FriendRequest.Status.ACCEPTED)
                .stream()
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getSender().getEmailId(),
                        req.getReceiver().getFirstName() + " " + req.getReceiver().getLastName(),
                        req.getStatus()))
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> getRejectedRequests() {
        return requestRepository.findByStatus(FriendRequest.Status.REJECTED)
                .stream()
                .map(req -> new FriendRequestDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getSender().getFirstName() + " " + req.getSender().getLastName(),
                        req.getReceiver().getFirstName() + " " + req.getReceiver().getLastName(),
                        req.getSender().getEmailId(),
                        req.getStatus()))
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> getAcceptedReceived(Long userId) {
        return mapToDTO(requestRepository.findAcceptedReceived(userId));
    }

    public List<FriendRequestDTO> getAcceptedSent(Long userId) {
        return mapToDTO(requestRepository.findAcceptedSent(userId));
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
        return requestRepository.findAllWithSenderAndReceiver()
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
    }

    public boolean areFriends(Long userId1, Long userId2) {
        Profile user1 = profileRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user1.getFriends().stream()
                .anyMatch(friend -> friend.getId().equals(userId2));
    }

    public List<FriendRequest> getPendingRequests(Long receiverId) {
        return requestRepository.findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING);
    }
}
