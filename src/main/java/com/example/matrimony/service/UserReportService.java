package com.example.matrimony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.entity.ArchivedChatMessage;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.ReportReason;
import com.example.matrimony.entity.UserReport;
import com.example.matrimony.repository.ArchivedChatRepository;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.ProfileViewLogRepository;
import com.example.matrimony.repository.UserReportRepository;

@Service
public class UserReportService {

    @Autowired
    private UserReportRepository reportRepo;

//    @Autowired
//    private ProfileRepository profileRepo;
//    @Autowired
//    private ChatMessageRepository messageRepository;
//    @Autowired
//    private ArchivedChatRepository archivedChatRepo;
//    @Autowired
//    private ProfileViewLogRepository profileViewLogRepository;
//    @Autowired
//    private ProfileService ProfileService;




	public Object reportUser(Long reporterId, Long reportedUserId, ReportReason reason, String description) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<UserReport> getAllReports() {
	    return reportRepo.findAll();
	}


}