package com.example.matrimony.service;

import com.example.matrimony.dto.AcceptedFriendCardDto;
import com.example.matrimony.dto.FriendDTO;
import com.example.matrimony.dto.FriendRequestCardDto;
import com.example.matrimony.dto.FriendRequestDTO;
import com.example.matrimony.dto.NotificationDto;
import com.example.matrimony.dto.ProfileCardDto;
import com.example.matrimony.entity.FriendRequest;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.Profilepicture;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class FriendRequestService {

    private final NotificationService notificationService;
    private final FriendRequestRepository requestRepository;
    private final ProfileRepository profileRepository;
    private final FriendRequestRepository friendRequestRepository;
    private static final Logger log =LoggerFactory.getLogger(FriendRequestService.class);

    public FriendRequestService(FriendRequestRepository requestRepository,
                                ProfileRepository profileRepository,
                                FriendRequestRepository friendRequestRepository,
                                NotificationService notificationService) {
        this.requestRepository = requestRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
        this.friendRequestRepository=friendRequestRepository;
        
    }

    // SEND FRIEND REQUEST
    public FriendRequest sendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You cannot send request to yourself");
        }

        Profile sender = getProfile(senderId, "Sender not found");
        Profile receiver = getProfile(receiverId, "Receiver not found");

        if (sender.getFriends().contains(receiver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already friends");
        }

        boolean alreadyPending = requestRepository
                .existsBySender_IdAndReceiver_IdAndStatus(
                        senderId, receiverId, FriendRequest.Status.PENDING);

        if (alreadyPending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Friend request already sent and pending");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequest.Status.PENDING);

        FriendRequest saved = requestRepository.save(friendRequest);

        NotificationDto notif = new NotificationDto();
        notif.setSenderId(sender.getId());
        notif.setReceiverId(receiver.getId());
        notif.setType("FRIEND_REQUEST_RECEIVED");
        notif.setMessage(sender.getFirstName() + " " + sender.getLastName() +
                " sent you a friend request");
        notif.setData(Map.of(
                "requestId", saved.getId(),
                "status", saved.getStatus().name()
        ));

        notificationService.sendToUserAndSave(notif);

        log.info("Friend request sent from {} to {}", senderId, receiverId);
        return saved;
    }
    // RECEIVED REQUEST
    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getReceivedRequests(Long receiverId) {

        return requestRepository
                .findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING)
                .stream()
                .map(this::mapToDetailedDTO)
                .toList();
    }

    // ACCEPT / REJECT REQUEST
    public FriendRequest respondToRequest(Long requestId, boolean accept) {

        FriendRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Friend request not found"));

        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This request already responded");
        }

        Profile sender = request.getSender();
        Profile receiver = request.getReceiver();

        if (accept) {
            request.setStatus(FriendRequest.Status.ACCEPTED);

            addFriendIfNotExists(sender, receiver);
            addFriendIfNotExists(receiver, sender);
        } else {
            request.setStatus(FriendRequest.Status.REJECTED);
        }

        FriendRequest updated = requestRepository.save(request);

        NotificationDto notif = new NotificationDto();
        notif.setSenderId(receiver.getId());
        notif.setReceiverId(sender.getId());
        notif.setType(accept ? "FRIEND_REQUEST_ACCEPTED" : "FRIEND_REQUEST_REJECTED");
        notif.setMessage(receiver.getFirstName() + " " + receiver.getLastName() +
                (accept ? " accepted " : " rejected ") + "your friend request");
        notif.setData(Map.of("requestId", requestId, "accepted", accept));

        notificationService.sendToUserAndSave(notif);

        log.info("Friend request {} processed. accepted={}", requestId, accept);
        return updated;
    }
    
    private String getPrimaryPhoto(Profile profile) {

        if (profile.getProfilePictures() == null || profile.getProfilePictures().isEmpty()) {
            return null;
        }

        return profile.getProfilePictures()
                .stream()
                .sorted((a, b) -> b.getUploadedAt().compareTo(a.getUploadedAt()))
                .findFirst()
                .map(Profilepicture::getFileName)   // correct field
                .orElse(null);
    }
    // SENT REQUESTS
    @Transactional(readOnly = true)
    public List<FriendDTO> getSentBy(Long senderId) {

        return requestRepository.findBySender_Id(senderId)
                .stream()
                .map(req -> new FriendDTO(
                        req.getId(),
                        req.getSender().getId(),
                        req.getReceiver().getId(),
                        req.getReceiver().getFirstName() + " " +
                                req.getReceiver().getLastName(),
                        req.getStatus().name()
                ))
                .toList();
    }

    // DELETE SENT REQUEST
    public String deleteSentRequest(Long requestId) {

        if (!requestRepository.existsById(requestId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Friend request not found");
        }

        requestRepository.deleteById(requestId);
        log.info("Friend request {} deleted", requestId);

        return "Friend request deleted successfully!";
    } 

    // ACCEPTED REQUESTS
    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getAcceptedRequests() {

        return requestRepository.findByStatus(FriendRequest.Status.ACCEPTED)
                .stream()
                .map(this::mapToDetailedDTO)
                .toList();
    }

    // REJECTED REQUESTS
    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getRejectedRequests() {

        return requestRepository.findByStatus(FriendRequest.Status.REJECTED)
                .stream()
                .map(this::mapToDetailedDTO)
                .toList();
    }

    // ACCEPTED RECEIVED/SENT
    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getAcceptedReceived(Long userId) {
        return mapToSimpleDTO(requestRepository.findAcceptedReceived(userId));
    }

    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getAcceptedSent(Long userId) {
        return mapToSimpleDTO(requestRepository.findAcceptedSent(userId));
    }

    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getRejectedReceived(Long userId) {
        return mapToSimpleDTO(requestRepository.findRejectedReceived(userId));
    }

    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getRejectedSent(Long userId) {
        return mapToSimpleDTO(requestRepository.findRejectedSent(userId));
    }

    // ALL FRIENDS
    @Transactional(readOnly = true)
    public List<FriendRequestDTO> getAllFriendRequests() {

        return requestRepository.findByStatus(FriendRequest.Status.ACCEPTED)
                .stream()
                .map(this::mapToDetailedDTO)
                .toList();
    }
    // ARE FRIENDS
    @Transactional(readOnly = true)
    public boolean areFriends(Long userId1, Long userId2) {

        Profile user1 = getProfile(userId1, "User not found");

        return user1.getFriends().stream()
                .anyMatch(friend -> friend.getId().equals(userId2));
    }

   
    // PENDING REQUESTS
    @Transactional(readOnly = true)
    public List<FriendRequest> getPendingRequests(Long receiverId) {
        return requestRepository
                .findByReceiver_IdAndStatus(receiverId, FriendRequest.Status.PENDING);
    }
    
    // PRIVATE HELPERS
    private Profile getProfile(Long id, String msg) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, msg));
    }

    private void addFriendIfNotExists(Profile user, Profile friend) {
        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            profileRepository.save(user);
        }
    }

    private List<FriendRequestDTO> mapToSimpleDTO(List<FriendRequest> list) {
        return list.stream().map(fr -> {
            FriendRequestDTO dto = new FriendRequestDTO();
            dto.setRequestId(fr.getId());
            dto.setStatus(fr.getStatus());
            dto.setSenderId(fr.getSender().getId());
            dto.setReceiverId(fr.getReceiver().getId());
            dto.setSenderName(fr.getSender().getFirstName() + " " +
                    fr.getSender().getLastName());
            dto.setReceiverName(fr.getReceiver().getFirstName() + " " +
                    fr.getReceiver().getLastName());
            return dto;
        }).toList();
    }

    private FriendRequestDTO mapToDetailedDTO(FriendRequest req) {

        FriendRequestDTO dto = new FriendRequestDTO();

        dto.setRequestId(req.getId());
        dto.setSenderId(req.getSender().getId());
        dto.setReceiverId(req.getReceiver().getId());
        dto.setSenderName(req.getSender().getFirstName() + " " + req.getSender().getLastName());
        dto.setReceiverName(req.getReceiver().getFirstName() + " " + req.getReceiver().getLastName());
        dto.setSenderEmail(req.getSender().getEmailId());
        dto.setStatus(req.getStatus());

        dto.setSenderCity(req.getSender().getCity());
        dto.setSenderAge(req.getSender().getAge());
        dto.setSenderPhoto(req.getSender().getUpdatePhoto());
        dto.setSenderGender(req.getSender().getGender());

        dto.setReceiverCity(req.getReceiver().getCity());
        dto.setReceiverAge(req.getReceiver().getAge());
        dto.setReceiverPhoto(req.getReceiver().getUpdatePhoto());
        dto.setReceiverGender(req.getReceiver().getGender());

        return dto;
    }
    @Transactional(readOnly = true)
    public List<FriendRequestCardDto> filterRequests(Long userId, String type) {

        List<FriendRequest> list;

        switch (type.toLowerCase()) {

            case "received":
                list = requestRepository
                        .findByReceiver_IdAndStatus(userId, FriendRequest.Status.PENDING);
                break;

            case "sent":
                list = requestRepository
                        .findBySender_IdAndStatus(userId, FriendRequest.Status.PENDING);
                break;

            case "accepted":
                list = requestRepository.findAcceptedBoth(userId);
                break;

            case "rejected":
                list = requestRepository.findRejectedBoth(userId);
                break;

            default:
                throw new RuntimeException("Invalid type");
        }

        // ⭐ ALWAYS RETURN OPPOSITE PROFILE
        return list.stream().map(fr -> {

            Profile other;

            if (fr.getSender().getId().equals(userId)) {
                other = fr.getReceiver();
            } else {
                other = fr.getSender();
            }

            // ===== PROFILE CARD DTO =====
            ProfileCardDto profileDto = new ProfileCardDto();
            profileDto.setId(other.getId());
            profileDto.setName(other.getFirstName() + " " + other.getLastName());
            profileDto.setAge(other.getAge());
            profileDto.setCity(other.getCity());
            profileDto.setUpdatePhoto(other.getUpdatePhoto());
            profileDto.setGender(other.getGender());
            profileDto.setPremium(other.isPremium());
            profileDto.setMotherTongue(other.getMotherTongue());
            profileDto.setCountry(other.getCountry());

            // ===== WRAPPER DTO =====
            FriendRequestCardDto card = new FriendRequestCardDto();
            card.setRequestId(fr.getId());
            card.setStatus(fr.getStatus().name());
            card.setProfile(profileDto);
            
            card.setSenderId(fr.getSender().getId());
            card.setReceiverId(fr.getReceiver().getId());

            return card;

        }).toList();
    }
    public List<AcceptedFriendCardDto> getAcceptedFriends(Long userId) {

        List<FriendRequest> requests =
                friendRequestRepository.findAllAccepted(userId);

        return requests.stream().map(fr -> {

            Profile sender = fr.getSender();
            Profile receiver = fr.getReceiver();

            Profile friend;
            Profile user;

            if (sender.getId().equals(userId)) {
                friend = receiver;
                user = sender;
            } else {
                friend = sender;
                user = receiver;
            }

            String friendPhoto = getPrimaryPhoto(friend);
            Boolean hideProfilePhoto = friend.getHideProfilePhoto();
            String gender = friend.getGender(); 

            if (Boolean.TRUE.equals(hideProfilePhoto)) {
                friendPhoto = null;
            }
            return new AcceptedFriendCardDto(
                    friend.getId(),
                    friend.getFirstName() + " " + friend.getLastName(),
                    friendPhoto,
                    gender,
                    hideProfilePhoto
                   
            );

        }).toList();
    }
}