package com.example.booking.domain.repository;

import com.example.booking.domain.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    List<Booking> findAll();

    List<Booking> findAll(int page, int limit);

    boolean existsOverlappingBooking(Long userId, Long tourId, LocalDate startDate, LocalDate endDate);

    void updateBookingStatus(Long bookingId, String status);

    List<Booking> findByUserId(Long userId, int page, int limit);
}