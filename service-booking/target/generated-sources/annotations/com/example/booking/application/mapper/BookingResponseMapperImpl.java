package com.example.booking.application.mapper;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.domain.model.Booking;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-01T01:54:05+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class BookingResponseMapperImpl implements BookingResponseMapper {

    @Override
    public BookingResponse toResponse(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        Long id = null;
        Long userId = null;
        Long tourId = null;
        Integer numberOfPeople = null;
        BigDecimal totalPrice = null;
        LocalDateTime bookingDate = null;
        String tourStartDate = null;
        String tourEndDate = null;
        String status = null;
        String rejectionReason = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = booking.getId();
        userId = booking.getUserId();
        tourId = booking.getTourId();
        numberOfPeople = booking.getNumberOfPeople();
        totalPrice = booking.getTotalPrice();
        bookingDate = booking.getBookingDate();
        if ( booking.getTourStartDate() != null ) {
            tourStartDate = DateTimeFormatter.ISO_LOCAL_DATE.format( booking.getTourStartDate() );
        }
        if ( booking.getTourEndDate() != null ) {
            tourEndDate = DateTimeFormatter.ISO_LOCAL_DATE.format( booking.getTourEndDate() );
        }
        status = booking.getStatus();
        rejectionReason = booking.getRejectionReason();
        createdAt = booking.getCreatedAt();
        updatedAt = booking.getUpdatedAt();

        String customerName = null;
        String customerEmail = null;

        BookingResponse bookingResponse = new BookingResponse( id, userId, customerName, customerEmail, tourId, numberOfPeople, totalPrice, bookingDate, tourStartDate, tourEndDate, status, rejectionReason, createdAt, updatedAt );

        return bookingResponse;
    }
}
