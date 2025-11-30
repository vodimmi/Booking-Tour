package com.example.booking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private Long tourId;
    private Integer numberOfPeople;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;
    private String tourStartDate;
    private String tourEndDate;
    private String status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;    
    private String specialRequirements;
}