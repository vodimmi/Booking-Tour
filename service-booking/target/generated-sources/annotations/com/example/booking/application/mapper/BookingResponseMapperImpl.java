package com.example.booking.application.mapper;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.domain.model.Booking;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T19:26:30+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class BookingResponseMapperImpl implements BookingResponseMapper {

    @Override
    public BookingResponse toResponse(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingResponse.BookingResponseBuilder bookingResponse = BookingResponse.builder();

        bookingResponse.bookingDate( booking.getBookingDate() );
        bookingResponse.createdAt( booking.getCreatedAt() );
        bookingResponse.id( booking.getId() );
        bookingResponse.numberOfPeople( booking.getNumberOfPeople() );
        bookingResponse.rejectionReason( booking.getRejectionReason() );
        bookingResponse.status( booking.getStatus() );
        bookingResponse.totalPrice( booking.getTotalPrice() );
        bookingResponse.tourId( booking.getTourId() );
        bookingResponse.updatedAt( booking.getUpdatedAt() );
        bookingResponse.userId( booking.getUserId() );

        return bookingResponse.build();
    }
}
