package com.example.booking.domain.command;

import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateBookingCommandHandler {
    private final BookingRepository bookingRepository;

    public Booking handle(CreateBookingCommand command) {
        // Create booking entity from command
        Booking booking = Booking.create(command);
        
        // Save to repository
        return bookingRepository.save(booking);
    }
}