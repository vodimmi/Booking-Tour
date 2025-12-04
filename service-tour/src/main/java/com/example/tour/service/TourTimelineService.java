package com.example.tour.service;

import com.example.tour.dto.TourTimelineDto;
import com.example.tour.entity.TourTimeline;
import com.example.tour.repository.TourTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourTimelineService {

    private final TourTimelineRepository timelineRepository;

    @Transactional(readOnly = true)
    public List<TourTimelineDto> getTimelineByTourId(Long tourId) {
        return timelineRepository.findByTourIdOrderByDayNumberAscStartTimeAsc(tourId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TourTimelineDto toDto(TourTimeline timeline) {
        return TourTimelineDto.builder()
                .id(timeline.getId())
                .tourId(timeline.getTourId())
                .dayNumber(timeline.getDayNumber())
                .title(timeline.getTitle())
                .description(timeline.getDescription())
                .startTime(timeline.getStartTime())
                .endTime(timeline.getEndTime())
                .build();
    }
}
