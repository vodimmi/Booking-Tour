package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.dto.CreatePaymentRequest;
import com.example.payment.dto.CreatePaymentResponse;
import com.example.payment.entity.Payment;
import com.example.payment.event.PaymentCompletedEvent;
import com.example.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This PaymentService contains a minimal implementation which *simulates* interaction with PayPal.
 * Replace simulated parts with real PayPal SDK calls when integrating with live credentials.
 */
@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${BOOKING_SERVICE_URL:}")
    private String bookingServiceUrl;

    public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate) {
        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * call PayPal Orders API to create an order and return the approval link from PayPal.
     */
    public CreatePaymentResponse createPayment(CreatePaymentRequest req) {
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
            Payment savedPayment = paymentRepository.save(p);
            
            // Publish payment completed event to RabbitMQ
            PaymentCompletedEvent event = new PaymentCompletedEvent(
                    savedPayment.getId(),
                    savedPayment.getBookingId(),
                    savedPayment.getPaypalOrderId(),
                    savedPayment.getAmount(),
                    savedPayment.getCurrency(),
                    savedPayment.getStatus()
            );
            
            log.info("Publishing payment completed event for booking ID: {}", savedPayment.getBookingId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_COMPLETED_QUEUE, event);
            
            return savedPayment;
        } else {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
}
