package com.example.tour.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TourCreateDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull
    private Double price;

    private String image;
    private Double rating = 0.0;
    private Integer reviewCount = 0;
    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long locationId;

    @Min(1)
    private Integer durationDays = 1;

    @Min(1)
    private Integer maxPeople = 1;

    @Min(0)
    private Integer availableSlots = 0;
}
