package com.example.matrimony.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayService {

    private final RazorpayClient client;
    private final Notificationadminservice notificationService;

    public RazorpayService(
            @Value("${razorpay.key_id}") String keyId,
            @Value("${razorpay.key_secret}") String keySecret,
            Notificationadminservice notificationService
    ) throws RazorpayException {
        this.client = new RazorpayClient(keyId, keySecret);
        this.notificationService = notificationService;
    }

    // ✅ CREATE ORDER WITH NOTIFICATION
    public Order createOrder(Long amountPaise, String receipt) throws RazorpayException {

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1);

        // ✅ Create order
        Order order = client.orders.create(orderRequest);

        // 🔔 Send Notification to Admin
        notificationService.sendOrderCreatedNotification(
                order.get("id"),
                amountPaise
        );

        return order;
    }

    // ✅ FETCH PAYMENT DETAILS
    public Payment fetchPayment(String paymentId) throws RazorpayException {
        return client.payments.fetch(paymentId);
    }
}
