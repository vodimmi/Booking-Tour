package com.example.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingCommand {
    private Long tourId;
    private Long userId;
    private Integer numberOfPeople;
    private LocalDateTime bookingDate;
    private String specialRequirements;
}