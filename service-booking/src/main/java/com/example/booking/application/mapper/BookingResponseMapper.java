package com.example.booking.application.mapper;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.domain.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingResponseMapper {
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "tourStartDate", source = "tourStartDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "tourEndDate", source = "tourEndDate", dateFormat = "yyyy-MM-dd")
    BookingResponse toResponse(Booking booking);
}
