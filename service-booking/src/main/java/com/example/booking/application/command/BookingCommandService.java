package com.example.booking.application.command;

import com.example.booking.application.dto.TourInfoDto;
import com.example.booking.application.dto.TourTimelineDto;
import com.example.booking.application.dto.UserInfoDto;
import com.example.booking.application.service.EmailService;
import com.example.booking.application.service.ExternalServiceClient;
import com.example.booking.domain.command.CreateBookingCommand;
import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import com.example.booking.domain.exception.BookingNotFoundException;
import com.example.booking.domain.exception.DuplicateBookingException;
import com.example.booking.infrastructure.messaging.BookingEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCommandService {
    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final EmailService emailService;
    private final ExternalServiceClient externalServiceClient;

    @Transactional
    public Long handleCreateBooking(CreateBookingCommand command) {
        validateBookingRequest(command);

        Booking booking = Booking.create(command);
        booking = bookingRepository.save(booking);

        eventPublisher.publishBookingCreatedEvent(booking);

        return booking.getId();
    }

    @Transactional
    public void handleConfirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        booking.confirm();
        bookingRepository.save(booking);

        eventPublisher.publishBookingConfirmedEvent(booking);

        // Send confirmation email with tour timeline and calendar link
        try {
            log.info("Sending booking confirmation email for bookingId: {}", bookingId);

            // Fetch user information
            UserInfoDto userInfo = externalServiceClient.getUserInfo(booking.getUserId());

            // Fetch tour information
            TourInfoDto tourInfo = externalServiceClient.getTourInfo(booking.getTourId());

            // Fetch tour timeline
            List<TourTimelineDto> timeline = externalServiceClient.getTourTimeline(booking.getTourId());

            // Send email
            emailService.sendBookingConfirmationEmail(
                    userInfo,
                    tourInfo,
                    timeline,
                    booking.getNumberOfPeople(),
                    booking.getTotalPrice(),
                    booking.getTourStartDate(),
                    booking.getTourEndDate());

            log.info("Booking confirmation email sent successfully for bookingId: {}", bookingId);
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email for bookingId: {}. Error: {}",
                    bookingId, e.getMessage(), e);
            // Don't throw exception to avoid transaction rollback
            // Email sending failure should not affect booking confirmation
        }
    }

    @Transactional
    public void handleCancelledBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        booking.cancel();
        bookingRepository.save(booking);

        eventPublisher.publishBookingCancelledEvent(booking);
    }

    @Transactional
    public void handleRejectBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        booking.reject(reason);
        bookingRepository.save(booking);

        eventPublisher.publishBookingRejectedEvent(booking, reason);
    }

    private void validateBookingRequest(CreateBookingCommand command) {
        if (command.getTourStartDate() == null || command.getTourEndDate() == null) {
            throw new IllegalArgumentException("Tour start date and end date are required");
        }

        if (command.getTourEndDate().isBefore(command.getTourStartDate())) {
            throw new IllegalArgumentException("Tour end date must be after start date");
        }

        if (bookingRepository.existsOverlappingBooking(
                command.getUserId(),
                command.getTourId(),
                command.getTourStartDate(),
                command.getTourEndDate())) {
            throw new DuplicateBookingException(
                    "You already have an active booking for this tour with overlapping dates. " +
                            "Please cancel your existing booking or choose different dates.");
        }
    }
}