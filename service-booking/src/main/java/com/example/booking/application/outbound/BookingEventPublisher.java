package com.example.booking.application.outbound;

import com.example.booking.domain.event.BookingEvent;
import com.example.booking.domain.model.Booking;
import com.example.booking.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class BookingEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishBookingCreatedEvent(Booking booking) {
        BookingEvent event = BookingEvent.bookingCreated(
            booking.getId(), 
            booking.getUserId(), 
            booking.getTourId()
        );
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
    }

    public void publishBookingConfirmedEvent(Booking booking) {
        BookingEvent event = BookingEvent.bookingConfirmed(
            booking.getId(), 
            booking.getUserId(), 
            booking.getTourId()
        );
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
    }

    public void publishBookingRejectedEvent(Booking booking, String reason) {
        BookingEvent event = BookingEvent.bookingRejected(
            booking.getId(), 
            booking.getUserId(), 
            booking.getTourId(),
            reason
        );
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
    }
}