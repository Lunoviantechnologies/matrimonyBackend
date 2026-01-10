package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ArchivedChatMessage;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.UserReport;
import com.example.matrimony.repository.ArchivedChatRepository;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.UserReportRepository;

@Service
public class AdminReportService {

	
	
	    @Autowired
	    private UserReportRepository reportRepo;

	    @Autowired
	    private ChatMessageRepository messageRepository;

	    @Autowired
	    private ArchivedChatRepository archivedChatRepo;

	    @Autowired
	    private ProfileService profileService;

	    @Transactional
	    public void deleteReportedProfileByReportId(Long reportId) {

	        UserReport report = reportRepo.findById(reportId)
	                .orElseThrow(() -> new RuntimeException("Report not found"));

	        Profile reporterProfile = report.getReporter();
	        Profile reportedProfile = report.getReportedUser();

	        Long profileA = reporterProfile.getId();
	        Long profileB = reportedProfile.getId();

	        // 1️⃣ Fetch chat
	        List<ChatMessage> chats =
	                messageRepository.findChatBetweenUsers(profileA, profileB);

	        // 2️⃣ Archive chat
	        List<ArchivedChatMessage> archivedChats = chats.stream().map(chat -> {
	            ArchivedChatMessage a = new ArchivedChatMessage();
	            a.setSenderId(chat.getSender().getId());
	            a.setReceiverId(chat.getReceiver().getId());
	            a.setMessage(chat.getMessage());
	            a.setSentAt(chat.getCreatedAt());
	            a.setDeletedUserId(profileB);
	            return a;
	        }).toList();

	        archivedChatRepo.saveAll(archivedChats);

	        // 3️⃣ Delete live chat
	        messageRepository.deleteBySenderIdOrReceiverId(profileA, profileB);

	        // 4️⃣ Delete profile using existing service
	        profileService.deleteById(profileB);
	    }
	}


