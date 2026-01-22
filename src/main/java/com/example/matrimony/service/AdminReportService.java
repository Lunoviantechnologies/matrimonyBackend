package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ArchivedChatMessage;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.DeletedProfile;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.UserReport;
import com.example.matrimony.repository.ArchivedChatRepository;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.DeletedProfileRepository;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.ProfileViewLogRepository;
import com.example.matrimony.repository.UserReportRepository;

@Service
public class AdminReportService {

    @Autowired private UserReportRepository reportRepo;
    @Autowired private ProfileRepository profileRepo;
    @Autowired private ChatMessageRepository chatRepo;
    @Autowired private ArchivedChatRepository archivedChatRepo;
    @Autowired private ProfileViewLogRepository profileViewLogRepo;
    @Autowired private FriendRequestRepository friendRequestRepo;
    @Autowired
    private DeletedProfileRepository deletedProfileRepo;

    
    @Transactional
    public void deleteReportedProfileByReportId(Long reportId) {

        UserReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        Profile reporter = report.getReporter();
        Profile reported = report.getReportedUser();

        Long reporterId = reporter.getId();
        Long reportedId = reported.getId();

        /* ==============================
           1️⃣ ARCHIVE CHAT
           ============================== */
        List<ChatMessage> chats =
                chatRepo.findChatBetweenUsers(reporterId, reportedId);

        List<ArchivedChatMessage> archived = chats.stream().map(c -> {
            ArchivedChatMessage a = new ArchivedChatMessage();
            a.setSenderId(c.getSender().getId());
            a.setReceiverId(c.getReceiver().getId());
            a.setMessage(c.getMessage());
            a.setSentAt(c.getCreatedAt());
            a.setDeletedUserId(reportedId);
            return a;
        }).toList();

        archivedChatRepo.saveAll(archived);
//        
        /* ==============================
        2️⃣ STORE DELETED PROFILE
        ============================== */
     DeletedProfile deletedProfile =
             mapToDeletedProfile(reported, report.getReason().name());

     deletedProfileRepo.save(deletedProfile);

        /* ==============================
           2️⃣ DELETE LIVE CHAT
           ============================== */
        chatRepo.deleteAllByProfileId(reportedId);

        /* ==============================
           3️⃣ CLEAN RELATED DATA
           ============================== */
        profileViewLogRepo.deleteAllByProfileId(reportedId);
        friendRequestRepo.deleteAllByProfileId(reportedId);
     //   friendRequestRepo.deleteAllByProfileId(reportedId);
        reportRepo.deleteAllByReportedUserId(reportedId);

        /* ==============================
           4️⃣ DELETE PROFILE (LAST)
           ============================== */
        profileRepo.deleteById(reportedId);
    }
    public UserReport getReportById(Long reportId) {
        return reportRepo.findById(reportId)
                .orElseThrow(() ->
                        new RuntimeException("Report not found with id: " + reportId)
                );
    }

    public void deleteReportById(Long reportId) {
        if (!reportRepo.existsById(reportId)) {
            throw new RuntimeException("User report not found with id: " + reportId);
        }
        reportRepo.deleteById(reportId);
    }
    
    public List<UserReport> getAllReports() {
        return reportRepo.findAll();
    }

	private DeletedProfile mapToDeletedProfile(Profile reported, String name) {
		// TODO Auto-generated method stub
		return null;
	}
}


	


