package com.example.matrimony.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.matrimony.dto.ContactRequest;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender mailSender;
	private final String adminEmail;
	private final String defaultFrom;

	public EmailService(JavaMailSender mailSender, @Value("${app.admin.email:}") String adminEmail,
			@Value("${app.mail.default-from:no-reply@vivahjeevan.com}") String defaultFrom) {
		this.mailSender = mailSender;
		this.adminEmail = adminEmail;
		this.defaultFrom = defaultFrom;
	}

	// =====================================================
	// COMMON EMAIL SENDER (REUSABLE)
	// =====================================================
	private void sendEmail(String to, String subject, String body, boolean html) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setFrom(new InternetAddress(defaultFrom, "VivahJeevan Team"));
			helper.setSubject(subject);
			helper.setText(body, html);

			mailSender.send(message);
			log.info("Email sent to {} | Subject={}", to, subject);

		} catch (Exception e) {
			log.error("Email failed to send to {}", to, e);
		}
	}

	private String footer() {
		int year = java.time.Year.now().getValue();
		return """
				<hr>
				<p style="font-size:12px;color:#777;">
				© %d VivahJeevan Pvt Ltd. All rights reserved.<br>
				This is an automated email. Please do not reply.<br>
				If you did not request this, please ignore.
				</p>
				""".formatted(year);
	}

	// =====================================================
	// CONTACT FORM EMAIL
	// =====================================================
	public void sendContactMessage(ContactRequest req) {

		if (adminEmail == null || adminEmail.isBlank()) {
			log.warn("Admin email not configured");
			return;
		}

		String subject = "New Contact Message - VivahJeevan";

		String body = """
				<h2>📩 New Contact Form Submission</h2>
				<p>You have received a new message from the VivahJeevan website.</p>

				<table border="1" cellpadding="10" style="border-collapse:collapse;">
				    <tr><td><b>Name</b></td><td>%s</td></tr>
				    <tr><td><b>Email</b></td><td>%s</td></tr>
				    <tr><td><b>Phone</b></td><td>%s</td></tr>
				    <tr><td><b>Message</b></td><td>%s</td></tr>
				</table>

				<br><hr>
				<p>This message was sent from VivahJeevan Contact Form.</p>
				""".formatted(req.getName(), req.getEmail(), req.getPhoneNumber(), req.getMessage());

		sendEmail(adminEmail, subject, body, true);
	}

	// =====================================================
	// PAYMENT SUCCESS EMAIL (PRODUCTION READY)
	// =====================================================
	public void sendPaymentSuccessEmail(String email, String name, String plan, BigDecimal amountRupees, String orderId,
			String paymentId, LocalDateTime premiumEnd) {

		if (email == null || email.isBlank()) {
			return;
		}

		// Amount is already in RUPEES (NO /100 conversion)
		String amountStr = NumberFormat.getCurrencyInstance(new Locale("en", "IN"))
				.format(amountRupees == null ? 0 : amountRupees);

		String expiryText = premiumEnd != null ? premiumEnd.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"))
				: "N/A";

		String subject = "🎉 Payment Successful - VivahJeevan Premium Activated";

		String body = """
				<h2>Dear %s,</h2>

				<p>We are happy to inform you that your payment was <b>successfully completed</b>.</p>

				<h3>💳 Payment Details</h3>
				<ul>
				    <li><b>Plan:</b> %s</li>
				    <li><b>Amount Paid:</b> %s</li>
				    <li><b>Order ID:</b> %s</li>
				    <li><b>Payment ID:</b> %s</li>
				    <li><b>Premium Valid Till:</b> %s</li>
				</ul>

				<p>You can now enjoy all premium features including:</p>
				<ul>
				    <li>Unlimited chat</li>
				    <li>View contact details</li>
				    <li>Priority matchmaking</li>
				</ul>

				<p>If you have any questions, contact our support team.</p>

				<br>
				<p>Warm Regards,<br>
				<b>VivahJeevan Team</b></p>

				<hr>
				<small>This is an automated email. Please do not reply.</small>
				""".formatted(name, plan, amountStr, orderId, paymentId, expiryText);

		sendEmail(email, subject, body, true);
	}

	// =====================================================
	// OTP EMAIL
	// =====================================================
	public void sendOtp(String toEmail, String otp) {

		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = "🔐 VivahJeevan OTP Verification";

		String body = """
				<h2>OTP Verification</h2>
				<p>Hello,</p>

				<p>Your One-Time Password (OTP) is:</p>
				<h1 style="color:blue;">%s</h1>

				<p>This OTP is valid for <b>10 minutes</b>. Do not share it with anyone.</p>

				<p>If you did not request this, please ignore this email.</p>

				<br>Regards,<br>
				<b>VivahJeevan Security Team</b>
				""".formatted(otp);

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// PROFILE APPROVAL EMAIL
	// =====================================================
	public void sendApprovalEmail(String toEmail, String name) {

		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = "Your VivahJeevan Profile is Approved";

		String body = """
				<h2>Dear %s,</h2>
				<p>Congratulations! 🎉 Your profile has been reviewed and approved by our admin team.</p>

				<p>You can now:</p>
				<ul>
				    <li>Send and receive messages</li>
				    <li>View contact details</li>
				    <li>Access premium matchmaking features</li>
				</ul>

				<p>We wish you success in finding your perfect match.</p>

				<br>Best Wishes,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name);

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// TICKET RESOLVED EMAIL
	// =====================================================
	public void sendTicketResolvedEmail(String toEmail, String name, Long ticketId) {
		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = " Your Support Ticket Has Been Resolved - VivahJeevan";

		String body = """
				<h2>Dear %s,</h2>

				<p>We are happy to inform you that your support ticket has been successfully resolved by our support team.</p>

				<h3>📄 Ticket Details</h3>
				<table border="1" cellpadding="8" style="border-collapse:collapse;">
				    <tr>
				        <td><b>Ticket ID</b></td>
				        <td>#%d</td>
				    </tr>
				    <tr>
				        <td><b>Status</b></td>
				        <td><span style="color:green;"><b>Resolved</b></span></td>
				    </tr>
				</table>

				<p>If you are satisfied with our resolution, no further action is required from your side.</p>

				<p>If you still face any issues or have additional questions, you can reply to this email or raise a new support ticket from your account dashboard.</p>

				<p>Thank you for choosing VivahJeevan. We are always here to help you find your perfect match.</p>

				<br>
				<p>Warm Regards,<br>
				<b>VivahJeevan Support Team</b></p>

				<hr>
				<p style="font-size:12px;color:gray;">
				This is an automated message. Please do not share sensitive information in reply.
				</p>
				"""
				.formatted(name, ticketId);

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// PROFILE REJECTION EMAIL
	// =====================================================
	public void sendRejectionEmail(String toEmail, String name, String reason) {

		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = " Profile Review Update - VivahJeevan";

		String body = """
				<h2>Dear %s,</h2>
				<p>Thank you for registering with VivahJeevan.</p>

				<p>Your profile was rejected due to the following reason:</p>
				<blockquote>%s</blockquote>

				<p>Please update your profile and resubmit for approval.</p>

				<br>Regards,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name, reason);

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// ACCOUNT DELETION EMAIL
	// =====================================================
	public void sendAccountDeletionEmail(String toEmail, String name) {

		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = " Account Deletion Requested - VivahJeevan";

		String body = """
				<h2>Dear %s,</h2>
				<p>Your account deletion request has been received.</p>

				<p>Your account will be permanently deleted after <b>30 days</b>.</p>
				<p>You can recover your account by logging in within 30 days.</p>

				<p>After deletion, all your data will be permanently removed.</p>

				<br>Regards,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name);

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// PLAN EXPIRED EMAIL
	// =====================================================
	public void sendPlanExpiredEmail(String toEmail, String name, LocalDateTime expiredAt) {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

		String subject = " Your Premium Plan Has Expired";

		String body = """
				<h2>Hi %s,</h2>
				<p>Your premium plan expired on <b>%s</b>.</p>

				<p>Renew now to continue enjoying premium matchmaking benefits.</p>

				<br>Regards,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name, expiredAt != null ? expiredAt.format(fmt) : "N/A");

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// PLAN EXPIRY REMINDER EMAIL
	// =====================================================
	public void sendPlanExpiryReminderEmail(String toEmail, String name, LocalDateTime expiryDate) {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

		String subject = " Premium Plan Expiring Soon";

		String body = """
				<h2>Hi %s,</h2>
				<p>Your premium plan will expire on <b>%s</b>.</p>

				<p>Renew today to avoid interruption in services.</p>

				<br>Regards,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name, expiryDate.format(fmt));

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// MANUAL PAYMENT SUCCESS (ADMIN ACTIVATION)
	// =====================================================
	public void sendManualPaymentSuccessEmail(String toEmail, String name, String planCode, Integer amountRupees,
			String paymentId, LocalDateTime premiumEnd) {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

		String subject = " Premium Plan Activated - VivahJeevan";

		String body = """
				<h2>Hi %s,</h2>
				<p>Your premium plan has been manually activated by admin.</p>

				<ul>
				<li>Plan: %s</li>
				<li>Amount Paid: ₹%d</li>
				<li>Payment ID: %s</li>
				<li>Expiry Date: %s</li>
				</ul>

				<p>Enjoy premium features!</p>

				<br>Regards,<br>
				<b>VivahJeevan Team</b>
				""".formatted(name, planCode, amountRupees, paymentId,
				premiumEnd != null ? premiumEnd.format(fmt) : "N/A");

		sendEmail(toEmail, subject, body, true);
	}

	// =====================================================
	// USER REGISTRATION SUCCESS EMAIL
	// =====================================================
	public void sendRegistrationSuccessEmail(String toEmail, String name) {

		if (toEmail == null || toEmail.isBlank()) {
			return;
		}

		String subject = "Welcome to VivahJeevan - Registration Successful";

		String body = """
				<h2>Dear %s,</h2>

				<p>Welcome to <b>VivahJeevan</b>! 🎉</p>
				<p>Your account has been <b>successfully registered</b>.</p>

				<h3> What You Can Do Now</h3>
				<ul>
				    <li>Create and update your profile</li>
				    <li>Browse matches and send interests</li>
				    <li>Chat with matches (Premium required)</li>
				    <li>Upgrade to premium for full features</li>
				</ul>

				<p>To get better matches, please complete your profile with accurate details and photos.</p>

				<p>If you have any questions, feel free to contact our support team.</p>

				<br>
				<p>Warm Regards,<br>
				<b>VivahJeevan Team</b></p>

				%s
				""".formatted(name, footer());

		sendEmail(toEmail, subject, body, true);
	}
	
	// Admin Email Services
	public void sendSimpleMail(String to, String subject, String body) {
	    SimpleMailMessage msg = new SimpleMailMessage();
	    msg.setTo(to);
	    msg.setSubject(subject);
	    msg.setText(body);
	    msg.setFrom("noreply@matrimony.com");
	    mailSender.send(msg);
	}


}
