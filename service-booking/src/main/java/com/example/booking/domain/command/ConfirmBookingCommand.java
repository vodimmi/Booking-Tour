package com.example.booking.domain.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmBookingCommand {
    private final Long bookingId;
}