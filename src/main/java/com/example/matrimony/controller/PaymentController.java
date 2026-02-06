package com.example.matrimony.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.*;

import com.example.matrimony.dto.CreateOrderRequest;
import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.SubscriptionPlan;
import com.example.matrimony.referral.ReferralService;
import com.example.matrimony.repository.PaymentRecordRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.repository.SubscriptionPlanRepository;
import com.example.matrimony.service.EmailService;
import com.example.matrimony.service.PaymentNotificationService;
import com.example.matrimony.service.PricingService;
import com.example.matrimony.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.Payment;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final RazorpayService razorpayService;
    private final PaymentRecordRepository paymentRepo;
    private final ProfileRepository profileRepo;
    private final PaymentNotificationService notificationService;
    private final EmailService emailService;
    private final SubscriptionPlanRepository subscriptionPlanRepo;
    private final PricingService pricingService;
    private final ReferralService referralService; // ✅ FIXED

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    public PaymentController(
            RazorpayService razorpayService,
            PaymentRecordRepository paymentRepo,
            ProfileRepository profileRepo,
            PaymentNotificationService notificationService,
            EmailService emailService,
            SubscriptionPlanRepository subscriptionPlanRepo,
            PricingService pricingService,
            ReferralService referralService // ✅ FIXED
    ) {
        this.razorpayService = razorpayService;
        this.paymentRepo = paymentRepo;
        this.profileRepo = profileRepo;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.subscriptionPlanRepo = subscriptionPlanRepo;
        this.pricingService = pricingService;
        this.referralService = referralService;
    }

    // ============================================================
    // CREATE ORDER
    // ============================================================
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {

        if (req == null || req.getProfileId() == null || req.getPlanCode() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_request"));
        }

        Profile profile = profileRepo.findById(req.getProfileId())
                .orElseThrow(() -> new RuntimeException("profile_not_found"));

        long baseRupees = pricingService.calculateFinalPrice(req.getPlanCode());

        long rewardBalance = profile.getReferralRewardBalance() != null
                ? profile.getReferralRewardBalance().longValue()
                : 0L;

        long referralDiscount = Math.min(baseRupees, rewardBalance);
        long finalRupees = baseRupees - referralDiscount;
        long paise = finalRupees * 100;

        PaymentRecord rec = new PaymentRecord();
        rec.setProfile(profile);
        rec.setUserId(profile.getId());
        rec.setName(getDisplayName(profile));
        rec.setPlanCode(req.getPlanCode());
        rec.setAmount(finalRupees);
        rec.setCurrency("INR");
        rec.setStatus("PENDING");
        rec.setCreatedAt(LocalDateTime.now());

        paymentRepo.save(rec);

        try {
            Order order = razorpayService.createOrder(paise, "receipt_" + rec.getId());
            rec.setRazorpayOrderId(order.get("id"));
            paymentRepo.save(rec);

            return ResponseEntity.ok(Map.of(
                    "razorpayOrderId", rec.getRazorpayOrderId(),
                    "razorpayKey", razorpayKeyId,
                    "amountRupees", finalRupees,
                    "currency", "INR",
                    "paymentRecordId", rec.getId()
            ));

        } catch (Exception e) {
            rec.setStatus("FAILED");
            paymentRepo.save(rec);
            log.error("Razorpay order failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "order_failed"));
        }
    }

    // ============================================================
    // VERIFY PAYMENT
    // ============================================================
    @PostMapping("/verify")
    @Transactional
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) throws Exception {

        String paymentId = body.get("razorpay_payment_id");
        String orderId = body.get("razorpay_order_id");
        String signature = body.get("razorpay_signature");

        if (paymentId == null || orderId == null || signature == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_parameters"));
        }

        PaymentRecord rec = paymentRepo.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("order_not_found"));

        if ("PAID".equals(rec.getStatus())) {
            return ResponseEntity.ok(Map.of("status", "already_verified"));
        }

        String generated = hmac(orderId + "|" + paymentId, razorpayKeySecret);
        if (!generated.equals(signature)) {
            rec.setStatus("FAILED");
            paymentRepo.save(rec);
            return ResponseEntity.badRequest().body(Map.of("error", "signature_invalid"));
        }

        Payment payment = razorpayService.fetchPayment(paymentId);

        rec.setTransactionId(paymentId);
        rec.setRazorpayPaymentId(paymentId);
        rec.setRazorpaySignature(signature);
        rec.setPaymentMode(payment.get("method"));
        rec.setStatus("PAID");
        paymentRepo.save(rec);

        Profile profile = rec.getProfile();
        if (profile != null) {

            SubscriptionPlan plan = subscriptionPlanRepo
                    .findByPlanCodeAndActiveTrue(rec.getPlanCode())
                    .orElseThrow(() -> new RuntimeException("plan_not_found"));

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime baseTime =
                    profile.getPremiumEnd() != null && profile.getPremiumEnd().isAfter(now)
                            ? profile.getPremiumEnd()
                            : now;

            profile.setPremium(true);

            if (profile.getPremiumStart() == null) {
                profile.setPremiumStart(now);
            }

            profile.setPremiumEnd(baseTime.plusMonths(plan.getDurationMonths()));

            BigDecimal referralBalance = profile.getReferralRewardBalance() != null
                    ? profile.getReferralRewardBalance()
                    : BigDecimal.ZERO;

            if (referralBalance.signum() > 0) {
                BigDecimal basePrice =
                        BigDecimal.valueOf(pricingService.calculateFinalPrice(rec.getPlanCode()));
                BigDecimal finalAmount =
                        BigDecimal.valueOf(rec.getAmount());

                BigDecimal discountToConsume =
                        referralBalance.min(
                                basePrice.subtract(finalAmount).max(BigDecimal.ZERO)
                        );

                profile.setReferralRewardBalance(
                        referralBalance.subtract(discountToConsume)
                );
            }

            profileRepo.save(profile);

            // ✅ MARK REFERRAL COMPLETED AFTER PAYMENT
            referralService.markReferralCompletedForUser(profile);
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        safeNotify(rec);
                    }
                }
        );

        return ResponseEntity.ok(Map.of("status", "success"));
    }

    // ============================================================
    // PAYMENT HISTORY
    // ============================================================
    @GetMapping("/successful/{profileId}")
    public List<PaymentDto> getPayments(@PathVariable Long profileId) {
        return paymentRepo.findByUserIdOrderByCreatedAtDesc(profileId)
                .stream().map(this::toDto).toList();
    }

    @GetMapping("/successful/{profileId}/latest")
    public ResponseEntity<?> getLatest(@PathVariable Long profileId) {
        return paymentRepo.findTopByUserIdOrderByCreatedAtDesc(profileId)
                .map(rec -> ResponseEntity.ok(toDto(rec)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private PaymentDto toDto(PaymentRecord rec) {
        PaymentDto dto = new PaymentDto();
        dto.setId(rec.getId());
        dto.setUserId(rec.getUserId());
        dto.setName(rec.getName());
        dto.setPlanCode(rec.getPlanCode());
        dto.setAmount(rec.getAmount());
        dto.setCurrency(rec.getCurrency());
        dto.setStatus(rec.getStatus());
        dto.setRazorpayOrderId(rec.getRazorpayOrderId());
        dto.setRazorpayPaymentId(rec.getRazorpayPaymentId());
        dto.setCreatedAt(rec.getCreatedAt());

        if (rec.getProfile() != null) {
            dto.setPremiumEnd(rec.getProfile().getPremiumEnd());
        }

        return dto;
    }

    private void safeNotify(PaymentRecord rec) {
        try {
            Profile profile = rec.getProfile();

            notificationService.notifyPaymentSuccess(
                    profile.getEmailId(),
                    profile.getMobileNumber(),
                    rec.getName(),
                    rec.getPlanCode(),
                    rec.getAmount(),
                    rec.getRazorpayOrderId(),
                    rec.getRazorpayPaymentId(),
                    profile.getPremiumEnd()
            );

            emailService.sendPaymentSuccessEmail(
                    profile.getEmailId(),
                    rec.getName(),
                    rec.getPlanCode(),
                    rec.getAmount(),
                    rec.getRazorpayOrderId(),
                    rec.getRazorpayPaymentId(),
                    profile.getPremiumEnd()
            );
        } catch (Exception e) {
            log.error("Notification failed", e);
        }
    }

    private String getDisplayName(Profile p) {
        return (p.getFirstName() + " " +
                Optional.ofNullable(p.getLastName()).orElse("")).trim();
    }

    private String hmac(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
