package com.example.booking.application.exception;

public class InvalidBookingStateException extends RuntimeException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}