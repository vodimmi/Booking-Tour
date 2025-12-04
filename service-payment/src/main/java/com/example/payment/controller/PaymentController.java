package com.example.payment.controller;

import com.example.payment.dto.CreatePaymentRequest;
import com.example.payment.dto.CreatePaymentResponse;
import com.example.payment.dto.PaymentStatusResponse;
import com.example.payment.entity.Payment;
import com.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponse> create(@RequestBody CreatePaymentRequest req) {
        return ResponseEntity.ok(paymentService.createPayment(req));
    }

    @PostMapping("/execute")
    public ResponseEntity<PaymentStatusResponse> execute(@RequestParam("orderId") String orderId) {
        Payment p = paymentService.executePayment(orderId);
        PaymentStatusResponse r = new PaymentStatusResponse();
        r.setPaymentId(p.getId());
        r.setStatus(p.getStatus());
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
