package com.example.tour.dto;

import lombok.Data;

@Data
public class TourResponseDto {
    private Long tourId;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long locationId;
    private String locationName;
    private Integer durationDays;
    private Integer maxPeople;
    private Integer availableSlots;
    private String status;
}
