package com.example.matrimony.service;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.matrimony.dto.ContactRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
    private final JavaMailSender mailSender;
    private final String adminEmail;
    private final String defaultFrom;
    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.admin.email:}") String adminEmail,
                        @Value("${app.mail.default-from:no-reply@example.com}") String defaultFrom) {
        this.mailSender = mailSender;
        this.adminEmail = adminEmail;
        this.defaultFrom = defaultFrom;
    }

    /** SEND CONTACT FORM EMAIL - WORKING VERSION */
    public void sendContactMessage(ContactRequest req) {
        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("app.admin.email not configured — skipping contact mail. From: {} <{}>", req.getName(), req.getEmail());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // --- FROM ---
            String safeFrom = "no-reply@matrimony.com";
            try {
                if (defaultFrom != null && !defaultFrom.isBlank()) {
                    InternetAddress address = new InternetAddress(defaultFrom, "Matrimony App");
                    address.validate(); // validate format
                    safeFrom = defaultFrom;
                }
            } catch (Exception e) {
                log.warn("Invalid defaultFrom email '{}', using {}", defaultFrom, safeFrom);
            }
            helper.setFrom(new InternetAddress(safeFrom, "Matrimony App"));

            // --- TO ---
            String safeTo = adminEmail;
            try {
                InternetAddress[] to = InternetAddress.parse(adminEmail, false);
                for (InternetAddress addr : to) addr.validate();
            } catch (Exception e) {
                log.warn("Invalid adminEmail '{}', using safe fallback '{}'", adminEmail, safeTo);
            }
            helper.setTo(InternetAddress.parse(safeTo, false));

            // --- REPLY-TO ---
            if (req.getEmail() != null && !req.getEmail().isBlank()) {
                try {
                    InternetAddress replyTo = new InternetAddress(req.getEmail());
                    replyTo.validate();
                    helper.setReplyTo(replyTo);
                } catch (Exception e) {
                    log.warn("Invalid reply-to email '{}', skipping", req.getEmail());
                }
            }

            // Subject and body
            helper.setSubject("New Contact Message from " + req.getName());
            StringBuilder sb = new StringBuilder();
            sb.append("<p>You have received a new message from the contact form.</p>");
            sb.append("<p><b>Name:</b> ").append(req.getName()).append("</p>");
            sb.append("<p><b>Email:</b> ").append(req.getEmail()).append("</p>");
            if (req.getPhoneNumber() != null) {
                sb.append("<p><b>Phone:</b> ").append(req.getPhoneNumber()).append("</p>");
            }
            sb.append("<p><b>Message:</b><br>").append(req.getMessage()).append("</p>");
            sb.append("<hr><p>-- End --</p>");
            helper.setText(sb.toString(), true);

            mailSender.send(message);
            log.info("Contact mail sent to {}", adminEmail);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send contact mail", e);
        }
    }



//    public void sendContactMessage(ContactRequest req) {
//        if (adminEmail == null || adminEmail.isBlank()) {
//            log.error("Admin email is not set. Cannot send contact message.");
//            return;
//        }
//
//        // Validate or fallback 'From' address
//        String fromAddress = (defaultFrom != null && !defaultFrom.isBlank()) 
//                ? defaultFrom 
//                : "no-reply@saathjaanam.com";
//
//        try {
//            // Validate email formats
//            InternetAddress from = new InternetAddress(fromAddress, "SaathJaanam Team>");
//            from.validate();  // Will throw AddressException if invalid
//
//            InternetAddress to = new InternetAddress(adminEmail);
//            to.validate();  // Will throw AddressException if invalid
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
//
//            helper.setTo(to);
//            helper.setFrom(from);
//            helper.setSubject("New Contact Message from " + req.getName());
//
//            StringBuilder sb = new StringBuilder();
//            sb.append("You have received a new message from the contact form.\n\n");
//            sb.append("Name: ").append(req.getName()).append("\n");
//            sb.append("Email: ").append(req.getEmail()).append("\n");
//            sb.append("Phone: ").append(req.getPhoneNumber()).append("\n\n");
//            sb.append("Message:\n").append(req.getMessage()).append("\n\n");
//            sb.append("---- End ----");
//
//            helper.setText(sb.toString(), false);  // plain text
//            mailSender.send(message);
//
//            log.info("Contact mail sent to {}", adminEmail);
//
//        } catch (Exception e) {
//            log.error("Failed to send contact mail", e);
//        }
//    }


    
//    public void sendContactMessage(ContactRequest req) {
//        if (adminEmail == null || adminEmail.isBlank()) {
//            log.warn("app.admin.email not configured — skipping contact mail. From: {} <{}>", req.getName(), req.getEmail());
//            return;
//        }
//
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(adminEmail);
//        msg.setFrom("SaathJaanam Team " + defaultFrom );
//        msg.setSubject("New Contact Message from " + req.getName());
//        msg.setFrom(defaultFrom); // just the email
//        StringBuilder sb = new StringBuilder();
//        sb.append("You have received a new message from the contact form.\n\n");
//        sb.append("Name: ").append(req.getName()).append("\n");
//        sb.append("Email: ").append(req.getEmail()).append("\n");
//        sb.append("Phone: ").append(req.getPhoneNumber()).append("\n\n");
//        sb.append("Message:\n").append(req.getMessage()).append("\n\n");
//        sb.append("---- End ----");
//
//        msg.setText(sb.toString());
//
//        try {
//            mailSender.send(msg);
//            log.info("Contact mail sent to {}", adminEmail);
//        } catch (MailException ex) {
//            log.error("Failed to send contact mail", ex);
//        }
//    }

    /** PAYMENT SUCCESS EMAIL (existing) */
    public void sendPaymentSuccessEmail(String toEmail,
                                        String name,
                                        String planCode,
                                        Long amountPaise,
                                        String razorpayOrderId,
                                        String razorpayPaymentId) {

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Payment email skipped — no recipient email for user {}", name);
            return;
        }

        double rupees = Objects.requireNonNullElse(amountPaise, 0L);
        String amountStr = NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(rupees);

        String subject = "Payment Successful - " + planCode;

        String body = String.format(
                "Hi %s,\n\nYour payment was successful!\n\n" +
                "Plan: %s\nAmount: %s\nOrder ID: %s\nPayment ID: %s\n\n" +
                "Thank you for choosing our service.\n",
                name,
                planCode,
                amountStr,
                razorpayOrderId,
                razorpayPaymentId
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">" );
        msg.setSubject(subject);
        msg.setText(body);

        try {
            mailSender.send(msg);
            log.info("Payment success email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send payment email to {} : {}", toEmail, e.getMessage(), e);
        }
    }
    
    
    /** *********************************************
     *  NEW — send password reset OTP email
     * ********************************************* */
    public void sendOtp(String toEmail, String otp) {

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("OTP email skipped — recipient email is empty");
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject("SaathJaanam OTP Services");
        msg.setText(
                "Your OTP is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\nSaathJaanam Team"
        );

        try {
            mailSender.send(msg);
            log.info("Password reset OTP sent to {}", toEmail);
        } catch (MailException ex) {
            log.error("Failed to send OTP email to {}", toEmail, ex);
        }
    }


    /** *********************************************
     *  NEW — send profile approval email
     * ********************************************* */
    public void sendApprovalEmail(String toEmail, String name) {
        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Approval email skipped — no recipient email for user {}", name);
            return;
        }

        String subject = "Your profile has been approved";
        String displayName = (name == null || name.isBlank()) ? "User" : name;
        String body = String.format(
                "Dear %s,\n\n" +
                "Congratulations — your profile on Matrimony has been approved by our admin team. You can now access all member features.\n\n" +
                "If you have any questions, reply to this email.\n\n" +
                "Regards,\nSaathJaanam Team",
                displayName
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject(subject);
        msg.setText(body);
        // optional: setFrom if your JavaMailSender supports it
        try {
            mailSender.send(msg);
            log.info("Approval email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send approval email to {} : {}", toEmail, e.getMessage(), e);
        }
    }
    /** *********************************************
     *  NEW — send ticket resolved email
     * ********************************************* */
    public void sendTicketResolvedEmail(String toEmail, String name, Long ticketId) {

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Ticket resolved email skipped — no recipient email for ticket {}", ticketId);
            return;
        }

        String displayName = (name == null || name.isBlank()) ? "User" : name;

        String subject = "Your Support Ticket Has Been Resolved";

        String body = String.format(
                "Dear %s,\n\n" +
                "Your support ticket (Ticket ID: %d) has been successfully resolved by our support team.\n\n" +
                "If you face any further issues, please feel free to raise a new support ticket.\n\n" +
                "Regards,\nSaathJaanam  Support Team",
                displayName,
                ticketId
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject(subject);
        msg.setText(body);

        try {
            mailSender.send(msg);
            log.info("Ticket resolved email sent to {} for ticket {}", toEmail, ticketId);
        } catch (Exception e) {
            log.error("Failed to send ticket resolved email to {} : {}", toEmail, e.getMessage(), e);
        }
    }
 

    /** *********************************************
     *  NEW — send profile rejection email
     * ********************************************* */
    public void sendRejectionEmail(String toEmail, String name, String reason) {
        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Rejection email skipped — no recipient email for user {}", name);
            return;
        }

        String subject = "Your profile has been rejected";
        String displayName = (name == null || name.isBlank()) ? "User" : name;
        String reasonText = (reason == null || reason.isBlank()) ? "insufficient or incomplete profile data" : reason;

        String body = String.format(
                "Dear %s,\n\n" +
                "We regret to inform you that your profile has been rejected by our admin team for the following reason:\n\n" +
                "%s\n\n" +
                "Please update your profile with the required information and re-submit. If you believe this is a mistake, please contact support.\n\n" +
                "Regards,\nSaathJaanam Team",
                displayName,
                reasonText
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject(subject);
        msg.setText(body);

        try {
            mailSender.send(msg);
            log.info("Rejection email sent to {} (reason: {})", toEmail, reasonText);
        } catch (Exception e) {
            log.error("Failed to send rejection email to {} : {}", toEmail, e.getMessage(), e);
        }
    }
    
    /** *********************************************
     *  NEW — send account deletion email (30-day window)
     * ********************************************* */
    public void sendAccountDeletionEmail(String toEmail, String name) {

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Account deletion email skipped — no recipient email for user {}", name);
            return;
        }

        String subject = "Account Deletion Requested";
        String displayName = (name == null || name.isBlank()) ? "User" : name;

        String body = String.format(
                "Dear %s,\n\n" +
                "We received a request to delete your account.\n\n" +
                "Your account has been disabled immediately and is scheduled for permanent deletion after 30 days.\n\n" +
                "If this request was made by mistake, you can recover your account within 30 days by logging in or contacting support.\n\n" +
                "After 30 days, all your data will be permanently removed from our system.\n\n" +
                "Regards,\nSaathJaanam Team",
                displayName
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject(subject);
        msg.setText(body);

        try {
            mailSender.send(msg);
            log.info("Account deletion email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send account deletion email to {} : {}", toEmail, e.getMessage(), e);
        }
    }
  //payment Expiry Logic 
    public void sendPlanExpiredEmail(
            String toEmail,
            String name,
            LocalDateTime expiredAt) {

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Your Premium Plan Has Expired");

        msg.setText("""
                Hi %s,

                Your premium plan expired on %s.

                You can renew anytime to continue enjoying premium features.

                Regards,
                Matrimony Team
                """.formatted(
                name,
                expiredAt != null ? expiredAt.format(fmt) : "N/A"
        ));

        mailSender.send(msg);
    }

    // Expiry before 3 days email services
    public void sendPlanExpiryReminderEmail(
            String toEmail,
            String name,
            LocalDateTime expiryDate) {

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Your Premium Plan Expires Soon");

        msg.setText("""
                Hi %s,

                Your premium plan will expire on %s.

                Renew now to continue enjoying premium features without interruption.

                Regards,
                Matrimony Team
                """.formatted(
                name,
                expiryDate.format(fmt)
        ));

        mailSender.send(msg);
    }
    
    //payment success email 
    public void sendPaymentSuccessEmail(
            String toEmail,
            String name,
            String planCode,
            Integer amountRupees,
            String paymentId,
            LocalDateTime premiumEnd) {

        if (toEmail == null || toEmail.isBlank()) {
            log.warn("Payment email skipped — no recipient email for user {}", name);
            return;
        }

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        String expiryText =
                premiumEnd != null
                        ? "Your plan will expire on " + premiumEnd.format(fmt)
                        : "No expiry information available.";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("SaathJaanam Team <" + defaultFrom + ">");
        msg.setSubject("Payment Successful – Plan Activated");

        msg.setText("""
                Hi %s,

                Your payment was successful.

                Plan        : %s
                Amount Paid : ₹%d
                Payment ID  : %s

                %s

                Thank you for choosing us.
                """.formatted(
                name,
                planCode,
                amountRupees,
                paymentId,
                expiryText
        ));

        try {
            mailSender.send(msg);
            log.info("Payment success email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send payment email", e);
        }
    }
}