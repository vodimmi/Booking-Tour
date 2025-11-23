package com.example.booking.domain.command;

import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import com.example.booking.application.exception.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmBookingCommandHandler {
    private final BookingRepository bookingRepository;

    public void handle(ConfirmBookingCommand command) {
        // Find booking
        Booking booking = bookingRepository.findById(command.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException(command.getBookingId()));

        // Update status
        booking.confirm();

        // Save changes
        bookingRepository.save(booking);
    }
}