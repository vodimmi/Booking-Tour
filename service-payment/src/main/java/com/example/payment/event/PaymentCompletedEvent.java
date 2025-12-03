package com.example.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent implements Serializable {
    private Long paymentId;
    private Long bookingId;
    private String paypalOrderId;
    private BigDecimal amount;
    private String currency;
    private String status;
}
