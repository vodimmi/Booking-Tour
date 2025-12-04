package com.example.tour.controller;

import com.example.tour.dto.TourCreateDto;
import com.example.tour.dto.TourResponseDto;
import com.example.tour.dto.TourTimelineDto;
import com.example.tour.dto.TourUpdateDto;
import com.example.tour.service.TourService;
import com.example.tour.service.TourTimelineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;
    private final TourTimelineService timelineService;

    public TourController(TourService tourService, TourTimelineService timelineService) {
        this.tourService = tourService;
        this.timelineService = timelineService;
    }

    @GetMapping
    public ResponseEntity<List<TourResponseDto>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<TourResponseDto> tours = tourService.findAll(page, limit);
        if (tours.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourResponseDto>> searchTours(
            @RequestParam("q") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<TourResponseDto> results = tourService.searchTours(keyword, page, limit);
        if (results.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<TourResponseDto> getOne(@PathVariable Long id) {
        return tourService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TourResponseDto> create(@Valid @RequestBody TourCreateDto dto) {
        TourResponseDto created = tourService.create(dto);
        return ResponseEntity.created(URI.create("/api/tours/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourResponseDto> update(@PathVariable Long id, @Valid @RequestBody TourUpdateDto dto) {
        return tourService.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TourResponseDto>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<TourResponseDto> tours = tourService.findByCategory(categoryId, page, limit);
        if (tours.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<List<TourTimelineDto>> getTourTimeline(@PathVariable Long id) {
        List<TourTimelineDto> timeline = timelineService.getTimelineByTourId(id);
        if (timeline.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(timeline);
    }

}
