package com.example.booking.application.command;

import com.example.booking.domain.command.CreateBookingCommand;
import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import com.example.booking.domain.exception.BookingNotFoundException;
import com.example.booking.domain.exception.DuplicateBookingException;
import com.example.booking.infrastructure.messaging.BookingEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingCommandService {
    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;

    @Transactional
    public Long handleCreateBooking(CreateBookingCommand command) {
        validateBookingRequest(command);

        Booking booking = Booking.create(command);
        booking = bookingRepository.save(booking);

        eventPublisher.publishBookingCreatedEvent(booking);

        return booking.getId();
    }

    @Transactional
    public void handleConfirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        booking.confirm();
        bookingRepository.save(booking);

        eventPublisher.publishBookingConfirmedEvent(booking);
    }

    @Transactional
    public void handleRejectBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        booking.reject(reason);
        bookingRepository.save(booking);

        eventPublisher.publishBookingRejectedEvent(booking, reason);
    }

    private void validateBookingRequest(CreateBookingCommand command) {
        if (bookingRepository.existsActiveBookingForUserAndTour(command.getUserId(), command.getTourId())) {
            throw new DuplicateBookingException("User already has an active booking for this tour");
        }
    }
}