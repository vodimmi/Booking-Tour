package com.example.booking.domain.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RejectBookingCommand {
    private final Long bookingId;
    private final String reason;
}