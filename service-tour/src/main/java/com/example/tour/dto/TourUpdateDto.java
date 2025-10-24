package com.example.tour.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TourUpdateDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Min(1)
    private Integer durationDays = 1;

    @Min(1)
    private Integer maxPeople = 1;

    @Min(0)
    private Integer availableSlots = 0;
}
