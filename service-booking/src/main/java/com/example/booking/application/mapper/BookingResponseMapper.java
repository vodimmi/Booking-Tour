package com.example.booking.application.mapper;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.domain.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingResponseMapper {
    BookingResponse toResponse(Booking booking);
}