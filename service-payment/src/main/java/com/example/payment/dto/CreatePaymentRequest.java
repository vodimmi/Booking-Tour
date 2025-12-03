package com.example.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    private Long bookingId;
    private BigDecimal amount;
    private String currency;
}
