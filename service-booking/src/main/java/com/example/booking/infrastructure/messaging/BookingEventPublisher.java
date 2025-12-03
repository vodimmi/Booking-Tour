package com.example.booking.infrastructure.messaging;

import com.example.booking.domain.model.Booking;
import com.example.booking.domain.event.BookingEvent;
import com.example.booking.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(BookingEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public BookingEventPublisher(@Autowired(required = false) RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        if (this.rabbitTemplate == null) {
            log.warn("RabbitTemplate is null. Events will not be published.");
        }
    }

    public void publishBookingCreatedEvent(Booking booking) {
        if (rabbitTemplate == null)
            return;

        BookingEvent event = BookingEvent.bookingCreated(
                booking.getId(),
                booking.getUserId(),
                booking.getTourId());
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (AmqpException ex) {
            log.warn("Failed to publish bookingCreated event for bookingId={}: {}", booking.getId(), ex.getMessage());
        }
    }

    public void publishBookingConfirmedEvent(Booking booking) {
        if (rabbitTemplate == null)
            return;

        BookingEvent event = BookingEvent.bookingConfirmed(
                booking.getId(),
                booking.getUserId(),
                booking.getTourId());
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (AmqpException ex) {
            log.warn("Failed to publish bookingConfirmed event for bookingId={}: {}", booking.getId(), ex.getMessage());
        }
    }

    public void publishBookingCancelledEvent(Booking booking) {
        if (rabbitTemplate == null)
            return;

        BookingEvent event = BookingEvent.bookingCancelled(
                booking.getId(),
                booking.getUserId(),
                booking.getTourId());
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (AmqpException ex) {
            log.warn("Failed to publish bookingCancelled event for bookingId={}: {}", booking.getId(), ex.getMessage());
        }
    }

    public void publishBookingRejectedEvent(Booking booking, String reason) {
        if (rabbitTemplate == null)
            return;

        BookingEvent event = BookingEvent.bookingRejected(
                booking.getId(),
                booking.getUserId(),
                booking.getTourId(),
                reason);
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);
        } catch (AmqpException ex) {
            log.warn("Failed to publish bookingRejected event for bookingId={}: {}", booking.getId(), ex.getMessage());
        }
    }
}