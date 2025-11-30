package com.example.booking.domain.command;

import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import com.example.booking.application.exception.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RejectBookingCommandHandler {
    private final BookingRepository bookingRepository;

    public void handle(RejectBookingCommand command) {
        // Find booking
        Booking booking = bookingRepository.findById(command.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException(command.getBookingId()));

        // Update status with reason
        booking.reject(command.getReason());

        // Save changes
        bookingRepository.save(booking);
    }
}