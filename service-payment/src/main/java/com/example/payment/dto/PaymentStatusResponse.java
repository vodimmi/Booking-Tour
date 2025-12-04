package com.example.payment.dto;

import lombok.Data;

@Data
public class PaymentStatusResponse {
    private Long paymentId;
    private String status;
}
