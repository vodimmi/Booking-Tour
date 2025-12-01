package com.example.booking.domain.command;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingCommand {
    private Long tourId;
    private Long userId;
    private Integer numberOfPeople;
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime bookingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tourStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tourEndDate;

    private String specialRequirements; // optional
}