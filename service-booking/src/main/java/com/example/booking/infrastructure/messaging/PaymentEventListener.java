package com.example.booking.infrastructure.messaging;

import com.example.booking.application.command.BookingCommandService;
import com.example.booking.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final BookingCommandService bookingCommandService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received payment completed event for booking ID: {}", event.getBookingId());
        try {
            bookingCommandService.handleConfirmBooking(event.getBookingId());
            log.info("Successfully confirmed booking ID: {}", event.getBookingId());
        } catch (Exception e) {
            log.error("Failed to confirm booking ID: {}", event.getBookingId(), e);
        }
    }
}
