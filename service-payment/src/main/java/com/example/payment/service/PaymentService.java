package com.example.payment.service;

import com.example.payment.dto.CreatePaymentRequest;
import com.example.payment.dto.CreatePaymentResponse;
import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * This PaymentService contains a minimal implementation which *simulates* interaction with PayPal.
 * Replace simulated parts with real PayPal SDK calls when integrating with live credentials.
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${BOOKING_SERVICE_URL:}")
    private String bookingServiceUrl; // optional, if you want to notify booking service

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Create a payment and return an approval url + fake order id.
     * In production, call PayPal Orders API to create an order and return the approval link from PayPal.
     */
    public CreatePaymentResponse createPayment(CreatePaymentRequest req) {
        // create fake paypal order id and approval url for demo
        String orderId = "ORDER-" + UUID.randomUUID();
        String approval = String.format("https://www.sandbox.paypal.com/checkoutnow?token=%s", orderId);

        Payment p = Payment.builder()
                .bookingId(req.getBookingId())
                .amount(req.getAmount() == null ? BigDecimal.ZERO : req.getAmount())
                .currency(req.getCurrency() == null ? "USD" : req.getCurrency())
                .status("CREATED")
                .paypalOrderId(orderId)
                .build();

        paymentRepository.save(p);

        CreatePaymentResponse resp = new CreatePaymentResponse();
        resp.setApprovalUrl(approval);
        resp.setOrderId(orderId);
        return resp;
    }

    /**
     * Execute payment (simulate). Updates status to COMPLETED and returns entity.
     * In real flow this would be called by your frontend after PayPal redirects back with tokens,
     * or by PayPal webhook when payment is captured.
     */
    public Payment executePayment(String orderId) {
        Optional<Payment> op = paymentRepository.findByPaypalOrderId(orderId);
        if (op.isPresent()) {
            Payment p = op.get();
            p.setStatus("COMPLETED");
            return paymentRepository.save(p);
        } else {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
    }
}
