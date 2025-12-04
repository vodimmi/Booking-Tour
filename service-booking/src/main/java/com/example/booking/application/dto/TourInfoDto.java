package com.example.booking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourInfoDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationDays;
    private String location;
    private String category;
}
