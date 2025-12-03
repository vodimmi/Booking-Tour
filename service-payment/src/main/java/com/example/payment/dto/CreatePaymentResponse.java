package com.example.payment.dto;

import lombok.Data;

@Data
public class CreatePaymentResponse {
    private String approvalUrl;
    private String orderId;
}
