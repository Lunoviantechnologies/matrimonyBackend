package com.example.matrimony.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentNotificationService.class);

    private final EmailService emailService;
    private final Msg91SmsService msg91SmsService;

    public PaymentNotificationService(EmailService emailService,
                                      Msg91SmsService msg91SmsService) {
        this.emailService = emailService;
        this.msg91SmsService = msg91SmsService;
    }

    /**
     * amountRupees = FINAL amount in RUPEES (already converted, no /100 here)
     */
    public void notifyPaymentSuccess(
            String toEmail,
            String phoneNumber,
            String name,
            String planCode,
            BigDecimal amountRupees,
            String razorpayOrderId,
            String razorpayPaymentId,
            LocalDateTime premiumEnd) {

        // ================= SEND EMAIL =================
        try {
            emailService.sendPaymentSuccessEmail(
                    toEmail,
                    name,
                    planCode,
                    amountRupees,          // ✅ RUPEES
                    razorpayOrderId,
                    razorpayPaymentId,
                    premiumEnd
            );
        } catch (Exception e) {
            log.error("Payment email failed for orderId={}", razorpayOrderId, e);
        }

        // ================= SEND SMS =================
        try {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                return;
            }

            String amountStr = NumberFormat
                    .getCurrencyInstance(new Locale("en", "IN"))
                    .format(amountRupees == null ? BigDecimal.ZERO : amountRupees);

            String sms = String.format(
                    "Hi %s, your payment of %s for plan %s was successful. " +
                    "Order ID: %s, Payment ID: %s. - VivahJeevan",
                    name,
                    amountStr,
                    planCode,
                    razorpayOrderId,
                    razorpayPaymentId
            );

            msg91SmsService.sendSms(phoneNumber, sms);

        } catch (Exception e) {
            log.error("Payment SMS failed for orderId={}", razorpayOrderId, e);
        }
    }
}
