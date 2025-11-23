package com.example.booking.application.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<String> handleBookingNotFound(BookingNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<String> handleDuplicateBooking(DuplicateBookingException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<String> handleInvalidState(InvalidBookingStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        // Log the exception so we have the full stacktrace in container logs for debugging
        log.error("Unhandled exception in request processing", ex);
        return ResponseEntity.internalServerError().body("An unexpected error occurred");
    }
}