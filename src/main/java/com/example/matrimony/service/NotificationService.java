package com.example.matrimony.service;

	import com.example.matrimony.dto.NotificationDto;
	import com.example.matrimony.entity.Notification;
	import com.example.matrimony.repository.NotificationRepository;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

	@Service
	public class NotificationService {

	    private final SimpMessagingTemplate messagingTemplate;
	    private final NotificationRepository repo;
	    private final ObjectMapper mapper;

	    public NotificationService(SimpMessagingTemplate messagingTemplate,
	                               NotificationRepository repo,
	                               ObjectMapper mapper) {
	        this.messagingTemplate = messagingTemplate;
	        this.repo = repo;
	        this.mapper = mapper;
	    }
	    @EventListener(ApplicationReadyEvent.class)
	    @Transactional
	    public void initialCleanup() {
	        cleanOldNotifications();
	    }

	    @Scheduled(cron = "0 0 1 * * ?")
	    @Transactional
	    public void cleanOldNotifications() {
	        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(1);
	        NotificationRepository.deleteByCreatedAtBefore(cutoffDate);
	        System.out.println("🧹 Old notifications cleaned up successfully at: " + LocalDateTime.now());
	    }

	    public NotificationDto sendToUserAndSave(NotificationDto dto) {
	        // Save notification entity
	        Notification entity = new Notification();
	        entity.setType(dto.getType());
	        entity.setMessage(dto.getMessage());
	        entity.setSenderId(dto.getSenderId());
	        entity.setReceiverId(dto.getReceiverId());
	        try {
	            if (dto.getData() != null) {
	                entity.setData(mapper.writeValueAsString(dto.getData()));
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); // log the error
	        }
	        Notification saved = repo.save(entity);

	        // Create DTO to return
	        NotificationDto out = new NotificationDto();
	        out.setId(saved.getId());
	        out.setType(saved.getType());
	        out.setMessage(saved.getMessage());
	        out.setSenderId(saved.getSenderId());
	        out.setReceiverId(saved.getReceiverId());
	        out.setData(saved.getData());
	        out.setRead(saved.isRead());
	        out.setCreatedAt(saved.getCreatedAt());

	        // Determine queue based on ID type
	        String queue;
	        if (dto.getReceiverId().equals(dto.getAdminId())) {
	            // Send to admin
	            queue = "/queue/notifications/admin";
	        } else {
	            // Send to normal user
	            queue = "/queue/notifications/user";
	        }

	        // Send notification
	        messagingTemplate.convertAndSendToUser(
	            String.valueOf(saved.getReceiverId()),
	            queue,
	            out
	        );

	        return out;
	    }


	    

	    public Page<Notification> getNotifications(Long receiverId, Pageable pageable) {
	        return repo.findByReceiverIdOrderByCreatedAtDesc(receiverId, pageable);
	    }

	    public long unreadCount(Long receiverId) {
	        return repo.countByReceiverIdAndIsReadFalse(receiverId);
	    }

	    public void markAsRead(Long notificationId, Long receiverId) {
	        Optional<Notification> opt = repo.findById(notificationId);
	        if (opt.isPresent() && opt.get().getReceiverId().equals(receiverId)) {
	            Notification n = opt.get();
	            n.setRead(true);
	            repo.save(n);
	        } else {
	    
	        }
	    }

	    public void markAllRead(Long receiverId) {
	        repo.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(receiverId)
	            .forEach(n -> { n.setRead(true); repo.save(n); });
	    }
	}


