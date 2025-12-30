package com.example.matrimony.service;

import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PaymentNotificationService {

    private final EmailService emailService;
    private final Msg91SmsService msg91SmsService;

    public PaymentNotificationService(EmailService emailService,
                                      Msg91SmsService msg91SmsService) {
        this.emailService = emailService;
        this.msg91SmsService = msg91SmsService;
    }

    /**
     * amountRupees: amount stored in DB (rupees, not paise)
     */
    public void notifyPaymentSuccess(String toEmail,
                                     String phoneNumber,
                                     String name,
                                     String planCode,
                                     Long amountRupees,
                                     String razorpayOrderId,
                                     String razorpayPaymentId) {

        // send email
        try {
            // pass rupees to email service
            emailService.sendPaymentSuccessEmail(toEmail, name, planCode, amountRupees, razorpayOrderId, razorpayPaymentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // send SMS
        try {
            // amountRupees already in rupees, no /100
            double rupees = amountRupees == null ? 0.0 : amountRupees.doubleValue();
            String amountStr = NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(rupees);

            String sms = String.format(
                    "Hi %s, your payment of %s for %s was successful. Order: %s, Payment: %s",
                    name, amountStr, planCode, razorpayOrderId, razorpayPaymentId
            );

            msg91SmsService.sendSms(phoneNumber, sms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
