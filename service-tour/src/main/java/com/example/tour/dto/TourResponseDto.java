package com.example.tour.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TourResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String category;
    private String image;
    private Double rating;
    private Integer reviews;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxParticipants;
    private Integer currentParticipants;
}
