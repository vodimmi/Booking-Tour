package com.example.booking.application.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private Long tourId;
    private Integer numberOfPeople;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;
    private String tourStartDate; // Tour start date (Ngày tham gia)
    private String status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}