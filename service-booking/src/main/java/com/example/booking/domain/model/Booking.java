package com.example.booking.domain.model;

import com.example.booking.domain.command.CreateBookingCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tour_id", nullable = false)
    private Long tourId;

    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Booking create(CreateBookingCommand command) {
        return Booking.builder()
                .userId(command.getUserId())
                .tourId(command.getTourId())
                .numberOfPeople(command.getNumberOfPeople())
                .totalPrice(command.getTotalPrice())
                .bookingDate(command.getBookingDate())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void confirm() {
        this.status = "CONFIRMED";
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = "REJECTED";
        this.rejectionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }
}