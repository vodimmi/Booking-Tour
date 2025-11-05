package com.example.tour.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TourUpdateDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    private Double price;
    private String image;
    private Double rating;
    private Integer reviewCount;
    private LocalDate startDate;
    private LocalDate endDate;

    @Min(1)
    private Integer durationDays = 1;

    @Min(1)
    private Integer maxPeople = 1;

    @Min(0)
    private Integer availableSlots = 0;

    private Long categoryId;
    private Long locationId;
}
