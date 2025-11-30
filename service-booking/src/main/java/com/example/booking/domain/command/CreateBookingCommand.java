package com.example.booking.domain.command;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingCommand {
    private Long tourId;
    private Long userId;
    private Integer numberOfPeople;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;
    private LocalDate tourStartDate;
    private LocalDate tourEndDate;
    private String specialRequirements;
}