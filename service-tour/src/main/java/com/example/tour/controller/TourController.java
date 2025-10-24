package com.example.tour.controller;

import com.example.tour.dto.TourCreateDto;
import com.example.tour.dto.TourResponseDto;
import com.example.tour.dto.TourUpdateDto;
import com.example.tour.service.TourService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public ResponseEntity<List<TourResponseDto>> getAll() {
        return ResponseEntity.ok(tourService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponseDto> getOne(@PathVariable Long id) {
        return tourService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TourResponseDto> create(@Valid @RequestBody TourCreateDto dto) {
        TourResponseDto created = tourService.create(dto);
        return ResponseEntity.created(URI.create("/api/tours/" + created.getTourId())).body(created);
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
}
