package com.example.booking.application.rest;

import com.example.booking.application.command.BookingCommandService;
import com.example.booking.application.query.BookingQueryService;
import com.example.booking.domain.command.CreateBookingCommand;
import com.example.booking.application.dto.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingCommandService commandService;
    private final BookingQueryService queryService;

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody CreateBookingCommand command) {
        Long bookingId = commandService.handleCreateBooking(command);
        return ResponseEntity.created(URI.create("/api/bookings/" + bookingId)).build();
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable("id") Long id) {
        commandService.handleConfirmBooking(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectBooking(
            @PathVariable("id") Long id,
            @RequestParam(value = "reason", required = true) String reason) {
        commandService.handleRejectBooking(id, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable("id") Long id) {
        return ResponseEntity.ok(queryService.getBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(queryService.getAllBookings());
    }
}