package com.example.booking.application.query;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.application.mapper.BookingResponseMapper;
import com.example.booking.domain.exception.BookingNotFoundException;
import com.example.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingQueryService {
    private final BookingRepository bookingRepository;
    private final BookingResponseMapper mapper;

    public BookingResponse getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}