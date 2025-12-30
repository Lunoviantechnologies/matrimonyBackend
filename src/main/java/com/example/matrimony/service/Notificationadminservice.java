package com.example.matrimony.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.entity.Notification;
import com.example.matrimony.repository.AdminRepository;
import com.example.matrimony.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Notificationadminservice {

    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public Notificationadminservice(AdminRepository adminRepository,
                                    NotificationRepository notificationRepository,
                                    SimpMessagingTemplate messagingTemplate,
                                    ObjectMapper objectMapper) {
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    // 🔔 SEND NOTIFICATION TO ALL ADMINS
    public void notifyAdmin(String type, String message, Object data) {
        List<Admin> admins = adminRepository.findAll();

        for (Admin admin : admins) {
            if (admin.getAdminId() == null) continue;

            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(message);
            notification.setSenderId(null); // system
            notification.setReceiverId(admin.getAdminId());
            notification.setRead(false);

            try {
                if (data != null) {
                    notification.setData(objectMapper.writeValueAsString(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Notification saved = notificationRepository.save(notification);

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(admin.getAdminId()),
                    "/queue/notifications",
                    saved
            );
        }
    }

    // 🔹 GET PAGED NOTIFICATIONS
    public Page<Notification> getNotifications(Long adminId, Pageable pageable) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(adminId, pageable);
    }

    // 🔹 UNREAD COUNT
    public long unreadCount(Long adminId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(adminId);
    }

    // 🔹 MARK SINGLE READ
    public void markAsRead(Long notificationId, Long adminId) {
        Optional<Notification> opt = notificationRepository.findById(notificationId);
        if (opt.isPresent() && opt.get().getReceiverId().equals(adminId)) {
            Notification n = opt.get();
            n.setRead(true);
            notificationRepository.save(n);
        }
    }

    // 🔹 MARK ALL READ
    public void markAllRead(Long adminId) {
        notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(adminId)
                .forEach(n -> {
                    n.setRead(true);
                    notificationRepository.save(n);
                });
    }

	public static void sendTicketNotification(String memberId, String string, String string2, Long id) {
		// TODO Auto-generated method stub
		
	}
	 public void sendOrderCreatedNotification(String orderId, Long amount) {

	        // 🔔 Example: Console log (replace with Email / FCM / DB)
	        System.out.println("🔔 Order Created Successfully");
	        System.out.println("Order ID: " + orderId);
	        System.out.println("Amount: ₹" + (amount / 100));

	        // 👉 Optional:
	        // sendEmail(...)
	        // sendPushNotification(...)
	        // saveNotificationToDatabase(...)
	    }

	public void createAdminNotification(String title, String message, Instant localDateTime) {
	    Notification notification = new Notification();
	    notification.setType(title);
	    notification.setMessage(message);
	    notification.setCreatedAt(localDateTime);
	    notification.setRead(false);

	    notificationRepository.save(notification);
	}

	

}
