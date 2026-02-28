//package com.example.matrimony.controller;
//
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.matrimony.entity.PaymentRecord;
//import com.example.matrimony.entity.Profile;
//import com.example.matrimony.entity.SubscriptionPlan;
//import com.example.matrimony.repository.PaymentRecordRepository;
//import com.example.matrimony.repository.ProfileRepository;
//import com.example.matrimony.repository.SubscriptionPlanRepository;
//import com.example.matrimony.service.EmailService;
//import com.example.matrimony.service.PaymentNotificationService;
//
//@RestController
//@RequestMapping("/api/webhook")
//public class PaymentWebhookController {
//
//    private static final Logger log =
//            LoggerFactory.getLogger(PaymentWebhookController.class);
//
//    private final PaymentRecordRepository paymentRepo;
//    private final ProfileRepository profileRepo;
//    private final SubscriptionPlanRepository planRepo;
//    private final PaymentNotificationService notificationService;
//    private final EmailService emailService;
//
//    @Value("${razorpay.webhook.secret}")
//    private String webhookSecret;
//
//    public PaymentWebhookController(
//            PaymentRecordRepository paymentRepo,
//            ProfileRepository profileRepo,
//            SubscriptionPlanRepository planRepo,
//            PaymentNotificationService notificationService,
//            EmailService emailService
//    ) {
//        this.paymentRepo = paymentRepo;
//        this.profileRepo = profileRepo;
//        this.planRepo = planRepo;
//        this.notificationService = notificationService;
//        this.emailService = emailService;
//    }
//    @PostMapping("/razorpay")
//    @Transactional
//    public ResponseEntity<String> handleWebhook(
//            @RequestHeader("X-Razorpay-Signature") String signature,
//            @RequestBody String payload
//    ) {
//
//        try {
//            if (!verifySignature(payload, signature)) {
//                log.error("Webhook signature verification FAILED");
//                return ResponseEntity.status(400).body("Invalid signature");
//            }
//
//            JSONObject json = new JSONObject(payload);
//            String event = json.getString("event");
//
//            log.info("Webhook received event={}", event);
//            if ("payment.captured".equals(event)) {
//                handlePaymentCaptured(json);
//            } else if ("payment.failed".equals(event)) {
//                handlePaymentFailed(json);
//            } else {
//                log.info("Unhandled webhook event={}", event);
//            }
//
//            return ResponseEntity.ok("Webhook processed");
//
//        } catch (Exception e) {
//            log.error("Webhook processing error", e);
//            return ResponseEntity.internalServerError().body("Webhook error");
//        }
//    }
//
//    private void handlePaymentCaptured(JSONObject json) {
//
//        JSONObject paymentEntity =
//                json.getJSONObject("payload")
//                        .getJSONObject("payment")
//                        .getJSONObject("entity");
//
//        String orderId = paymentEntity.getString("order_id");
//        String paymentId = paymentEntity.getString("id");
//        String method = paymentEntity.optString("method", "unknown");
//
//        log.info("Payment captured orderId={} paymentId={}", orderId, paymentId);
//
//        Optional<PaymentRecord> optional =
//                paymentRepo.findByRazorpayOrderId(orderId);
//
//        if (optional.isEmpty()) {
//            log.error("PaymentRecord not found for orderId={}", orderId);
//            return;
//        }
//
//        PaymentRecord rec = optional.get();
//        if ("PAID".equals(rec.getStatus())) {
//            log.info("Payment already processed orderId={}", orderId);
//            return;
//        }
//        rec.setStatus("PAID");
//        rec.setRazorpayPaymentId(paymentId);
//        rec.setPaymentMode(method);
//        rec.setTransactionId(paymentId);
//
//        paymentRepo.save(rec);
//        applyPremium(rec);
//        safeNotify(rec);
//
//        log.info("Payment processed successfully orderId={}", orderId);
//    }
//
//    private void handlePaymentFailed(JSONObject json) {
//
//        JSONObject paymentEntity =
//                json.getJSONObject("payload")
//                        .getJSONObject("payment")
//                        .getJSONObject("entity");
//
//        String orderId = paymentEntity.getString("order_id");
//
//        Optional<PaymentRecord> optional =
//                paymentRepo.findByRazorpayOrderId(orderId);
//
//        if (optional.isEmpty()) {
//            log.warn("PaymentRecord not found for failed orderId={}", orderId);
//            return;
//        }
//
//        PaymentRecord rec = optional.get();
//
//        // ❗ Do not override PAID
//        if ("PAID".equals(rec.getStatus())) {
//            log.warn("Ignoring FAILED event for already PAID orderId={}", orderId);
//            return;
//        }
//
//        rec.setStatus("FAILED");
//        paymentRepo.save(rec);
//
//        log.info("Payment marked FAILED orderId={}", orderId);
//    }
//    private void applyPremium(PaymentRecord rec) {
//
//        Profile profile = rec.getProfile();
//        if (profile == null) {
//            log.error("Profile not found for paymentId={}", rec.getId());
//            return;
//        }
//
//        SubscriptionPlan plan = planRepo
//                .findByPlanCodeAndActiveTrue(rec.getPlanCode())
//                .orElseThrow(() -> new IllegalStateException(
//                        "Active subscription plan not found for code: "
//                                + rec.getPlanCode()
//                ));
//
//        long months = plan.getDurationMonths();
//        LocalDateTime now = LocalDateTime.now();
//
//        LocalDateTime base =
//                profile.getPremiumEnd() != null &&
//                        profile.getPremiumEnd().isAfter(now)
//                        ? profile.getPremiumEnd()
//                        : now;
//
//        profile.setPremium(true);
//
//        if (profile.getPremiumStart() == null) {
//            profile.setPremiumStart(now);
//        }
//
//        profile.setPremiumEnd(base.plusMonths(months));
//
//        profileRepo.save(profile);
//
//        log.info("Premium applied to profileId={}", profile.getId());
//    }
//    private void safeNotify(PaymentRecord rec) {
//
//        try {
//
//            Profile profile = rec.getProfile();
//            if (profile == null) return;
//
//            BigDecimal amount = rec.getAmount();
//
//            notificationService.notifyPaymentSuccess(
//                    profile.getEmailId(),
//                    profile.getMobileNumber(),
//                    rec.getName(),
//                    rec.getPlanCode(),
//                    amount,
//                    rec.getRazorpayOrderId(),
//                    rec.getRazorpayPaymentId(),
//                    profile.getPremiumEnd()
//            );
//
//            emailService.sendPaymentSuccessEmail(
//                    profile.getEmailId(),
//                    rec.getName(),
//                    rec.getPlanCode(),
//                    amount,
//                    rec.getRazorpayOrderId(),
//                    rec.getRazorpayPaymentId(),
//                    profile.getPremiumEnd()
//            );
//
//        } catch (Exception e) {
//            log.error("Notification failed for paymentId={}", rec.getId(), e);
//        }
//    }
//    private boolean verifySignature(String payload, String actualSignature)
//            throws Exception {
//
//        Mac mac = Mac.getInstance("HmacSHA256");
//
//        mac.init(new SecretKeySpec(
//                webhookSecret.getBytes(StandardCharsets.UTF_8),
//                "HmacSHA256"
//        ));
//
//        byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//
//        StringBuilder hex = new StringBuilder();
//        for (byte b : raw) {
//            hex.append(String.format("%02x", b));
//        }
//
//        return MessageDigest.isEqual(
//                hex.toString().getBytes(StandardCharsets.UTF_8),
//                actualSignature.getBytes(StandardCharsets.UTF_8)
//        );
//    }
//}