package com.example.matrimony.service;

import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PaymentNotificationService.class);

    private final EmailService emailService;
    private final Msg91SmsService msg91SmsService;

    public PaymentNotificationService(EmailService emailService,
                                      Msg91SmsService msg91SmsService) {
        this.emailService = emailService;
        this.msg91SmsService = msg91SmsService;
    }

    /**
     * amountRupees = DB stored rupees (NOT paise)
     */
    public void notifyPaymentSuccess(String toEmail,
                                     String phoneNumber,
                                     String name,
                                     String planCode,
                                     Long amountRupees,
                                     String razorpayOrderId,
                                     String razorpayPaymentId,
                                     LocalDateTime premiumEnd) {

        // ================= SEND EMAIL =================
        try {
            emailService.sendPaymentSuccessEmail(
                    toEmail,
                    name,
                    planCode,
                    amountRupees,          // RUPEES
                    razorpayOrderId,
                    razorpayPaymentId,
                    premiumEnd             // ✅ FIXED
            );
        } catch (Exception e) {
            log.error("Payment email failed", e);
        }

        // ================= SEND SMS =================
        try {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                return;
            }

            double rupees = amountRupees == null ? 0.0 : amountRupees.doubleValue();
            String amountStr = NumberFormat.getCurrencyInstance(new Locale("en", "IN"))
                    .format(rupees);

            String sms = String.format(
                    "Hi %s, your payment of %s for plan %s was successful. Order ID: %s, Payment ID: %s. - VivahJeevan",
                    name,
                    amountStr,
                    planCode,
                    razorpayOrderId,
                    razorpayPaymentId
            );

            msg91SmsService.sendSms(phoneNumber, sms);

        } catch (Exception e) {
            log.error("Payment SMS failed", e);
        }
    }
}