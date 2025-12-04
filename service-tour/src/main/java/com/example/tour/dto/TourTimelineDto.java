package com.example.tour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourTimelineDto {
    private Long id;
    private Long tourId;
    private Integer dayNumber;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
}
