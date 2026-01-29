package com.example.matrimony.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.CreateOrderRequest;
import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.entity.SubscriptionPlan;
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
   
    private final EmailService emailService;
    
    private final PricingService pricingService;
    
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final RazorpayService razorpayService;
    private final PaymentRecordRepository paymentRepo;
    private final ProfileRepository profileRepo;
    private final PaymentNotificationService notificationService;

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
            SubscriptionPlanRepository subscriptionPlanRepository,
            PricingService pricingService) {
        this.razorpayService = razorpayService;
        this.paymentRepo = paymentRepo;
        this.profileRepo = profileRepo;
        this.notificationService = notificationService;
        this.emailService=emailService;
        this.pricingService=pricingService;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    // ============================================================
    // CREATE ORDER
    // ============================================================
    @PostMapping("/create-order")
    
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {

        // ================== VALIDATION ==================
        if (req == null || req.getProfileId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "missing_profileId"));
        }

        if (req.getPlanCode() == null || req.getPlanCode().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "invalid_planCode"));
        }

        // ================== PROFILE ==================
        Profile profile = profileRepo.findById(req.getProfileId())
                .orElseThrow(() -> new RuntimeException("profile_not_found"));

        // ================== PRICE RESOLUTION (IMPORTANT) ==================
        // ✅ THIS REPLACES PLAN_PRICES_RUPEES COMPLETELY
       

     // ================== PRICE RESOLUTION (IMPORTANT) ==================
     // ✅ SINGLE SOURCE OF TRUTH FOR MONEY
     long rupees = pricingService.calculateFinalPrice(req.getPlanCode());
     long paise = rupees * 100;


  // ================== CREATE PAYMENT RECORD ==================
     PaymentRecord rec = new PaymentRecord();
     rec.setProfile(profile);
     rec.setUserId(profile.getId());
     rec.setName(getDisplayName(profile));

     // ✅ Plan code comes from request (single source)
     rec.setPlanCode(req.getPlanCode());

     // ✅ Amount from pricing service
     rec.setAmount((int) rupees);   // or change column to Long (recommended)
     rec.setCurrency("INR");
     rec.setStatus("PENDING");
     rec.setCreatedAt(LocalDateTime.now());

     paymentRepo.save(rec);


        // ================== RAZORPAY ORDER ==================
        try {
            Order order = razorpayService.createOrder(
                    paise,
                    "receipt_" + rec.getId()
            );

            rec.setRazorpayOrderId(order.get("id"));
            paymentRepo.save(rec);

            return ResponseEntity.ok(Map.of(
                    "razorpayOrderId", rec.getRazorpayOrderId(),
                    "razorpayKey", razorpayKeyId,
                    "amountRupees", rupees,
                    "currency", "INR",
                    "paymentRecordId", rec.getId()
            ));

        } catch (Exception ex) {

            rec.setStatus("FAILED");
            paymentRepo.save(rec);

            log.error("Error creating Razorpay order", ex);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "payment_order_failed"));
        }
    }


    // ============================================================
    // VERIFY PAYMENT
    // ============================================================
//    @PostMapping("/verify")
//    @Transactional
//    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) throws Exception {
//
//        String paymentId = body.get("razorpay_payment_id");
//        String orderId = body.get("razorpay_order_id");
//        String signature = body.get("razorpay_signature");
//
//        if (paymentId == null || orderId == null || signature == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "missing_parameters"));
//        }
//
//        PaymentRecord rec = paymentRepo.findByRazorpayOrderId(orderId)
//                .orElseThrow(() -> new RuntimeException("order_not_found"));
//
//        // ✅ IDEMPOTENCY CHECK
//        if ("PAID".equals(rec.getStatus())) {
//            return ResponseEntity.ok(Map.of("status", "already_verified"));
//        }
//
//        String generated = hmac(orderId + "|" + paymentId, razorpayKeySecret);
//
//        if (!generated.equals(signature)) {
//            rec.setStatus("FAILED");
//            paymentRepo.save(rec);
//            // ❌ payment failed → premium false
//            Profile profile = rec.getProfile();
//            if (profile != null) {
//                profile.setPremium(false);
//            }
//            return ResponseEntity.badRequest().body(Map.of("error", "signature_mismatch"));
//        }
//
//        // 🔍 Fetch payment details from Razorpay (INTERNAL ONLY)
//        Payment payment = razorpayService.fetchPayment(paymentId);
//
//        rec.setTransactionId(paymentId);
//        rec.setRazorpayPaymentId(paymentId);
//        rec.setRazorpaySignature(signature);
//        rec.setPaymentMode(payment.get("method")); // CARD / UPI / NETBANKING
//        rec.setStatus("PAID");
//        
//     // ✅ payment success → premium true
//        Profile profile1 = rec.getProfile();
//        if (profile1 != null) {
//            profile1.setPremium(true);
//            profile1.setPremiumStart(LocalDateTime.now());
//            profile1.setPremiumEnd(LocalDateTime.now().plusMonths(1)); // example
//        }
//        paymentRepo.save(rec);
//
//        TransactionSynchronizationManager.registerSynchronization(
//                new TransactionSynchronization() {
//                    @Override
//                    public void afterCommit() {
//                        safeNotify(rec);
//                    }
//                }
//        );
//
//        return ResponseEntity.ok(Map.of("status", "success"));
//    }
    
    @PostMapping("/verify")
    @Transactional
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) throws Exception {

        // ================== VALIDATION ==================
        String paymentId = body.get("razorpay_payment_id");
        String orderId   = body.get("razorpay_order_id");
        String signature = body.get("razorpay_signature");

        if (paymentId == null || orderId == null || signature == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "missing_parameters"));
        }

        // ================== FETCH PAYMENT RECORD ==================
        PaymentRecord rec = paymentRepo.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("order_not_found"));

        // ================== IDEMPOTENCY ==================
        if ("PAID".equals(rec.getStatus())) {
            return ResponseEntity.ok(Map.of("status", "already_verified"));
        }

        // ================== SIGNATURE VERIFICATION ==================
        String generated = hmac(orderId + "|" + paymentId, razorpayKeySecret);

        if (!generated.equals(signature)) {

            rec.setStatus("FAILED");
            paymentRepo.save(rec);

            Profile profile = rec.getProfile();
            if (profile != null) {
                profile.setPremium(false);
                profile.setPremiumEnd(LocalDateTime.now());
                profileRepo.save(profile);
            }

            return ResponseEntity.badRequest()
                    .body(Map.of("error", "signature_mismatch"));
        }

        // ================== FETCH PAYMENT DETAILS ==================
        Payment payment = razorpayService.fetchPayment(paymentId);

        rec.setTransactionId(paymentId);
        rec.setRazorpayPaymentId(paymentId);
        rec.setRazorpaySignature(signature);
        rec.setPaymentMode(payment.get("method"));
        rec.setStatus("PAID");

        paymentRepo.save(rec);

     // ================== APPLY PREMIUM USING DB PLAN ==================
        Profile profile = rec.getProfile();
        if (profile != null) {

            // ✅ FETCH PLAN DIRECTLY (NO PRICING LOGIC HERE)
            SubscriptionPlan plan = subscriptionPlanRepository
                    .findByPlanCodeAndActiveTrue(rec.getPlanCode())
                    .orElseThrow(() -> new RuntimeException("Invalid subscription plan"));

            long months = plan.getDurationMonths();
            LocalDateTime now = LocalDateTime.now();

            // ✅ EXTEND IF ACTIVE ELSE START NEW
            LocalDateTime baseTime =
                    profile.getPremiumEnd() != null &&
                    profile.getPremiumEnd().isAfter(now)
                            ? profile.getPremiumEnd()
                            : now;

            profile.setPremium(true);
            profile.setPremiumStart(now);
            profile.setPremiumEnd(baseTime.plusMonths(months));

            profileRepo.save(profile);
        }

        // ================== POST-COMMIT NOTIFICATIONS ==================
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
    // USER READ APIs
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
    // ADMIN APIs
    // ============================================================
//    @GetMapping("/successful")
//    public List<PaymentDto> getAllSuccessfulPayments() {
//        return paymentRepo.findByStatusOrderByCreatedAtDesc("PAID")
//                .stream().map(this::toDto).toList();
//    }
    @GetMapping("/successful")
    public List<PaymentDto> getAllPaymentsByStatuses() {
        List<String> statuses = List.of("PAID", "CREATED", "FAILED");
        return paymentRepo.findByStatusInOrderByCreatedAtDesc(statuses)
                          .stream()
                          .map(this::toDto)
                          .toList();
    }


    // admin: payment by paymentId
    @GetMapping("/admin/successful/{paymentId}")
    public ResponseEntity<?> getSuccessfulPaymentById(@PathVariable Long paymentId) {
        return paymentRepo.findByIdAndStatus(paymentId, "PAID")
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
        dto.setAmount(Math.toIntExact(rec.getAmount()));
        dto.setCurrency(rec.getCurrency());
        dto.setStatus(rec.getStatus());
        dto.setRazorpayOrderId(rec.getRazorpayOrderId());
        dto.setRazorpayPaymentId(rec.getRazorpayPaymentId());
        dto.setCreatedAt(rec.getCreatedAt());
        if (rec.getProfile() != null) {
            LocalDateTime premiumEnd = rec.getProfile().getPremiumEnd();
            dto.setPremiumEnd(premiumEnd);
            dto.setExpiryMessage(buildExpiryMessage(premiumEnd));
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
                    Long.valueOf(rec.getAmount()),
                    rec.getRazorpayOrderId(),
                    rec.getRazorpayPaymentId()
            );

            // ✅ USE EXISTING EmailService
            emailService.sendPaymentSuccessEmail(
                    profile.getEmailId(),
                    rec.getName(),
                    rec.getPlanCode(),
                    Math.toIntExact(rec.getAmount()), 
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
    
    // Expiry logic for sending the messages
    private String buildExpiryMessage(LocalDateTime premiumEnd) {

        if (premiumEnd == null) {
            return "No active plan";
        }

        if (premiumEnd.isBefore(LocalDateTime.now())) {
            return "Your plan has expired";
        }

        return "Your plan will expire on " +
                premiumEnd.toLocalDate() +
                " at " +
                premiumEnd.toLocalTime().withSecond(0).withNano(0);
    }
    

}