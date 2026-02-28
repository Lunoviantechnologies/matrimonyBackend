package com.example.matrimony.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.dto.DeletedProfileBackupDTO;
import com.example.matrimony.dto.DynamicYearlyReportResponse;
import com.example.matrimony.dto.PlanYearlyStats;
import com.example.matrimony.dto.UserReportResponse;
import com.example.matrimony.entity.*;
import com.example.matrimony.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class AdminReportService {

    @Autowired private UserReportRepository reportRepo;
    @Autowired private ProfileRepository profileRepo;
    @Autowired private ChatMessageRepository chatRepo;
    @Autowired private ArchivedChatRepository archivedChatRepo;
    @Autowired private ProfileViewLogRepository profileViewLogRepo;
    @Autowired private FriendRequestRepository friendRequestRepo;
    @Autowired private DeletedProfileSnapshotRepository snapshotRepo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentRecordRepository paymentRecordRepository;
    @Autowired
    private UserBlockRepository chatBlockRepo;
    
    
    private DeletedProfileBackupDTO buildBackup(
            Profile profile,
            List<ChatMessageDto> chatDtos
    ) {

        DeletedProfileBackupDTO dto = new DeletedProfileBackupDTO();

        dto.setUserId(profile.getId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getEmailId());
        dto.setMobile(profile.getMobileNumber());
        dto.setGender(profile.getGender());
        dto.setCreatedAt(profile.getCreatedAt());

        dto.setChats(chatDtos);

        dto.setDeletedAt(LocalDateTime.now());
        dto.setDeletedByAdmin("ADMIN");
        dto.setDeleteReason("Permanent delete");

        return dto;
    }



    // ================= GET PENDING REPORTS =================
    public List<UserReport> getPendingReports() {
        return reportRepo.findByStatus(ReportStatus.PENDING);
    }

    // ================= APPROVE REPORT =================
    @Transactional
    public void approveReport(Long reportId, String adminComment) {

        UserReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new RuntimeException("Report already processed");
        }

        Profile reported = report.getReportedUser();
        Profile reporter = report.getReporter();

        Long reporterId = reporter.getId();
        Long reportedId = reported.getId();

        // Archive chats
        List<ChatMessage> chats = chatRepo.findChatBetweenUsers(reporterId, reportedId);

        List<ArchivedChatMessage> archived = chats.stream().map(c -> {
            ArchivedChatMessage a = new ArchivedChatMessage();
            a.setSenderId(c.getSender().getId());
            a.setReceiverId(c.getReceiver().getId());
            a.setMessage(c.getMessage());
            a.setSentAt(c.getCreatedAt());
            a.setDeletedUserId(reportedId);
            return a;
        }).collect(Collectors.toList());

        archivedChatRepo.saveAll(archived);

        // Delete chats
        chatRepo.deleteAllByProfileId(reportedId);

        // Clean social logs
        profileViewLogRepo.deleteAllByProfileId(reportedId);
        friendRequestRepo.deleteAllByProfileId(reportedId);

        // Soft ban user
        reported.setActive(false);
        reported.setBanned(true);
        reported.setBannedAt(LocalDateTime.now());
        reported.setBanReason(adminComment);
        profileRepo.save(reported);

        // Update report
        report.setStatus(ReportStatus.ACTION_TAKEN);
        report.setReviewedAt(LocalDateTime.now());
        report.setAdminComment(adminComment);
        reportRepo.save(report);
    }

    // ================= REJECT REPORT =================
    @Transactional
    public void rejectReport(Long reportId, String adminComment) {

        UserReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(ReportStatus.REJECTED);
        report.setReviewedAt(LocalDateTime.now());
        report.setAdminComment(adminComment);

        reportRepo.save(report);
    }

   
 // ================= PERMANENT DELETE WITH FULL BACKUP =================
    
    @Transactional
    public void permanentDeleteUser(Long userId) {

        // 1️⃣ Fetch profile
        Profile profile = profileRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

      
        // 3️⃣ DELETE BLOCK RELATIONS
        chatBlockRepo.deleteAllByBlockedId(userId);
        chatBlockRepo.deleteAllByBlockerId(userId);

        // 4️⃣ FETCH chats for BACKUP
        List<ChatMessageDto> chatDtos = chatRepo
                .findAllBySender_IdOrReceiver_Id(userId, userId)
                .stream()
                .map(ChatMessageDto::fromEntity)
                .toList();

        // 5️⃣ BUILD BACKUP
        DeletedProfileBackupDTO backupDTO = buildBackup(profile, chatDtos);

        String fullJson;
        try {
            fullJson = objectMapper.writeValueAsString(backupDTO);
        } catch (Exception e) {
            throw new RuntimeException("Backup JSON failed", e);
        }

        // 6️⃣ SAVE SNAPSHOT
        DeletedProfileSnapshot snapshot = new DeletedProfileSnapshot();
        snapshot.setUserId(userId);
        snapshot.setFullProfileJson(fullJson);
        snapshot.setDeletedAt(LocalDateTime.now());
        snapshot.setDeletedByAdmin("ADMIN");
        snapshot.setDeleteReason("Permanent delete");

        snapshotRepo.save(snapshot);

        // 7️⃣ DELETE DEPENDENT DATA
        chatRepo.deleteAllBySender_IdOrReceiver_Id(userId, userId);
        profileViewLogRepo.deleteAllByProfileId(userId);
        friendRequestRepo.deleteAllByProfileId(userId);
        reportRepo.deleteAllByReportedUser_Id(userId);

        // 8️⃣ FINALLY DELETE PROFILE
        profileRepo.delete(profile);
    }
    
    
 // ================= GET ALL REPORTS =================
    public List<UserReportResponse> getAllReports() {

        List<UserReport> reports = reportRepo.findAll();

        return reports.stream()
                .map(this::mapToDto)
                .toList();
    }
    private UserReportResponse mapToDto(UserReport report) {

        UserReportResponse dto = new UserReportResponse();

        dto.setId(report.getId());
        dto.setReporterId(report.getReporter().getId());
        dto.setReporterName(report.getReporter().getFirstName());
        dto.setReportedUserId(report.getReportedUser().getId());
        dto.setReportedUserName(report.getReportedUser().getFirstName());
        dto.setReason(report.getReason());
        dto.setDescription(report.getDescription());
        dto.setStatus(report.getStatus());
        dto.setReportedAt(report.getReportedAt());
        dto.setReviewedAt(report.getReviewedAt());
        dto.setAdminComment(report.getAdminComment());

        return dto;
    }

    //
//    @Transactional
//    public void banUser(Long userId, String reason) {
//
//        Profile profile = profileRepo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        profile.setBanned(true);
//        profile.setActive(false);    
//        profile.setBannedAt(LocalDateTime.now());
//        profile.setBanReason(reason);
//
//        profileRepo.save(profile);
//
//       reportRepo.updateStatusByReportedUser(userId, ReportStatus.BANNED);
//    }
    @Transactional
    public void banUser(Long userId, String reason, String adminComment) {

        Profile profile = profileRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setBanned(true);
        profile.setActive(false);
        profile.setBannedAt(LocalDateTime.now());
        profile.setBanReason(reason);
        profileRepo.save(profile);

        // ✅ Save admin comment in reports
        reportRepo.updateStatusAndCommentByReportedUser(
                userId,
                ReportStatus.BANNED,
                adminComment
        );
    }

    public DynamicYearlyReportResponse getYearlyDashboard(int year) {

        List<PlanYearlyStats> plans =
                paymentRecordRepository.getYearlyPlanStats(year);

        double totalRevenue = plans.stream()
                .mapToDouble(p -> p.getTotalRevenue())
                .sum();

        return new DynamicYearlyReportResponse(
                year,
                plans,
                totalRevenue
        );
    }
//    @Transactional
//    public void permanentDeleteUser(Long userId) {
//
//        // 1️⃣ Fetch profile
//        Profile profile = profileRepo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2️⃣ REMOVE ALL BLOCK REFERENCES (BOTH SIDES)
//        chatBlockRepo.deleteAllByBlockedId(userId);
//        chatBlockRepo.deleteAllByBlockerId(userId);
//
//        // ✅ CRITICAL: force execution of block deletes
//        chatBlockRepo.flush();
//
//        // 3️⃣ FETCH chats for BACKUP
//        List<ChatMessage> chats =
//                chatRepo.findAllBySender_IdOrReceiver_Id(userId, userId);
//
//        List<ChatMessageDto> chatDtos = chats.stream()
//                .map(ChatMessageDto::fromEntity)
//                .toList();
//
//        // 4️⃣ Build backup DTO
//        DeletedProfileBackupDTO backupDTO = buildBackup(profile, chatDtos);
//
//        String fullJson;
//        try {
//            fullJson = objectMapper.writeValueAsString(backupDTO);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create backup JSON", e);
//        }
//
//        // 5️⃣ Save snapshot
//        DeletedProfileSnapshot snapshot = new DeletedProfileSnapshot();
//        snapshot.setUserId(userId);
//        snapshot.setFullProfileJson(fullJson);
//        snapshot.setDeletedAt(LocalDateTime.now());
//        snapshot.setDeletedByAdmin("ADMIN");
//        snapshot.setDeleteReason("Permanent delete");
//
//        snapshotRepo.save(snapshot);
//
//        // 6️⃣ Delete remaining dependencies
//        chatRepo.deleteAllBySender_IdOrReceiver_Id(userId, userId);
//        profileViewLogRepo.deleteAllByProfileId(userId);
//        friendRequestRepo.deleteAllByProfileId(userId);
//        reportRepo.deleteAllByReportedUser_Id(userId);
//
//        // 7️⃣ FINAL DELETE (NOW FK SAFE)
//        profileRepo.delete(profile);
//    }

}