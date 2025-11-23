package com.example.booking.domain.event;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingEvent {
    private String eventType;
    private Long bookingId;
    private Long userId;
    private Long tourId;
    private String status;
    private String reason;
    private LocalDateTime timestamp;

    public static BookingEvent bookingCreated(Long bookingId, Long userId, Long tourId) {
        return BookingEvent.builder()
                .eventType("BOOKING_CREATED")
                .bookingId(bookingId)
                .userId(userId)
                .tourId(tourId)
                .status("PENDING")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BookingEvent bookingConfirmed(Long bookingId, Long userId, Long tourId) {
        return BookingEvent.builder()
                .eventType("BOOKING_CONFIRMED")
                .bookingId(bookingId)
                .userId(userId)
                .tourId(tourId)
                .status("CONFIRMED")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BookingEvent bookingRejected(Long bookingId, Long userId, Long tourId, String reason) {
        return BookingEvent.builder()
                .eventType("BOOKING_REJECTED")
                .bookingId(bookingId)
                .userId(userId)
                .tourId(tourId)
                .status("REJECTED")
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }
}