//package com.example.matrimony.controller;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.Map;
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
//    private static final Logger log = LoggerFactory.getLogger(PaymentWebhookController.class);
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
//
//    // =========================================================
//    // RAZORPAY WEBHOOK ENTRY POINT (PRODUCTION)
//    // =========================================================
//    @PostMapping("/razorpay")
//    @Transactional
//    public ResponseEntity<String> handleWebhook(
//            @RequestHeader("X-Razorpay-Signature") String signature,
//            @RequestBody String payload
//    ) {
//
//        try {
//            // 1. VERIFY SIGNATURE
//            if (!verifySignature(payload, signature)) {
//                log.error("Webhook signature verification FAILED");
//                return ResponseEntity.status(400).body("Invalid signature");
//            }
//
//            JSONObject json = new JSONObject(payload);
//            String event = json.getString("event");
//
//            log.info("Webhook received event={}", event);
//
//            if ("payment.captured".equals(event)) {
//                handlePaymentCaptured(json);
//            }
//
//            if ("payment.failed".equals(event)) {
//                handlePaymentFailed(json);
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
//    // =========================================================
//    // PAYMENT SUCCESS HANDLER
//    // =========================================================
//    private void handlePaymentCaptured(JSONObject json) {
//
//        JSONObject paymentEntity =
//                json.getJSONObject("payload")
//                    .getJSONObject("payment")
//                    .getJSONObject("entity");
//
//        String orderId = paymentEntity.getString("order_id");
//        String paymentId = paymentEntity.getString("id");
//        String method = paymentEntity.optString("method", "unknown");
//
//        log.info("Payment captured orderId={} paymentId={}", orderId, paymentId);
//
//        Optional<PaymentRecord> optional = paymentRepo.findByRazorpayOrderId(orderId);
//        if (optional.isEmpty()) {
//            log.error("PaymentRecord not found for orderId={}", orderId);
//            return;
//        }
//
//        PaymentRecord rec = optional.get();
//
//        // ===== IDEMPOTENCY CHECK =====
//        if ("PAID".equals(rec.getStatus())) {
//            log.info("Payment already processed orderId={}", orderId);
//            return;
//        }
//
//        // ===== UPDATE PAYMENT =====
//        rec.setStatus("PAID");
//        rec.setRazorpayPaymentId(paymentId);
//        rec.setPaymentMode(method);
//        rec.setTransactionId(paymentId);
//        paymentRepo.save(rec);
//
//        // ===== APPLY PREMIUM =====
//        applyPremium(rec);
//
//        // ===== SEND EMAIL + SMS =====
//        safeNotify(rec);
//
//        log.info("Payment processed successfully orderId={}", orderId);
//    }
//
//    // =========================================================
//    // PAYMENT FAILED HANDLER
//    // =========================================================
//    private void handlePaymentFailed(JSONObject json) {
//
//        JSONObject paymentEntity =
//                json.getJSONObject("payload")
//                    .getJSONObject("payment")
//                    .getJSONObject("entity");
//
//        String orderId = paymentEntity.getString("order_id");
//
//        Optional<PaymentRecord> optional = paymentRepo.findByRazorpayOrderId(orderId);
//        if (optional.isEmpty()) return;
//
//        PaymentRecord rec = optional.get();
//        rec.setStatus("FAILED");
//        paymentRepo.save(rec);
//
//        log.info("Payment marked FAILED orderId={}", orderId);
//    }
//
//    // =========================================================
//    // APPLY PREMIUM TO PROFILE
//    // =========================================================
//    private void applyPremium(PaymentRecord rec) {
//
//        Profile profile = rec.getProfile();
//        if (profile == null) return;
//
//        SubscriptionPlan plan = planRepo
//                .findByPlanCodeAndActiveTrue(rec.getPlanCode())
//                .orElseThrow(() -> new RuntimeException("Plan not found"));
//
//        long months = plan.getDurationMonths();
//        LocalDateTime now = LocalDateTime.now();
//
//        LocalDateTime base =
//                profile.getPremiumEnd() != null &&
//                profile.getPremiumEnd().isAfter(now)
//                ? profile.getPremiumEnd()
//                : now;
//
//        profile.setPremium(true);
//
//        if (profile.getPremiumStart() == null) {
//            profile.setPremiumStart(now);
//        }
//
//        profile.setPremiumEnd(base.plusMonths(months));
//        profileRepo.save(profile);
//
//        log.info("Premium applied to profileId={}", profile.getId());
//    }
//
//    // =========================================================
//    // SEND EMAIL + SMS SAFELY
//    // =========================================================
//    private void safeNotify(PaymentRecord rec) {
//        try {
//            Profile profile = rec.getProfile();
//            if (profile == null) return;
//
//            BigDecimal amountRupees = rec.getAmount(); // already rupees
//
//            notificationService.notifyPaymentSuccess(
//                    profile.getEmailId(),
//                    profile.getMobileNumber(),
//                    rec.getName(),
//                    rec.getPlanCode(),
//                    amountRupees,
//                    rec.getRazorpayOrderId(),
//                    rec.getRazorpayPaymentId(),
//                    profile.getPremiumEnd()
//            );
//
//            emailService.sendPaymentSuccessEmail(
//                    profile.getEmailId(),
//                    rec.getName(),
//                    rec.getPlanCode(),
//                    amountRupees,
//                    rec.getRazorpayOrderId(),
//                    rec.getRazorpayPaymentId(),
//                    profile.getPremiumEnd()
//            );
//
//        } catch (Exception e) {
//            log.error("Notification failed for paymentId={}", rec.getId(), e);
//        }
//    }
//
//
//
//    // =========================================================
//    // SIGNATURE VERIFICATION
//    // =========================================================
//    private boolean verifySignature(String payload, String actualSignature) throws Exception {
//
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
//
//        byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//
//        StringBuilder hex = new StringBuilder();
//        for (byte b : raw) {
//            hex.append(String.format("%02x", b));
//        }
//
//        return hex.toString().equals(actualSignature);
//    }
//}
