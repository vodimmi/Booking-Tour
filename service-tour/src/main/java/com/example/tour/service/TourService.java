package com.example.tour.service;

import com.example.tour.dto.TourCreateDto;
import com.example.tour.dto.TourResponseDto;
import com.example.tour.dto.TourUpdateDto;
import com.example.tour.entity.Location;
import com.example.tour.entity.Tour;
import com.example.tour.entity.TourCategory;
import com.example.tour.repository.LocationRepository;
import com.example.tour.repository.TourCategoryRepository;
import com.example.tour.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final TourCategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    public TourService(TourRepository tourRepository,
                       TourCategoryRepository categoryRepository,
                       LocationRepository locationRepository) {
        this.tourRepository = tourRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    public List<TourResponseDto> findAll() {
        return tourRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<TourResponseDto> findById(Long id) {
        return tourRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public TourResponseDto create(TourCreateDto dto) {
        TourCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategoryId()));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + dto.getLocationId()));

        Tour t = new Tour();
        t.setName(dto.getName());
        t.setDescription(dto.getDescription());
        t.setCategory(category);
        t.setLocation(location);
        t.setDurationDays(dto.getDurationDays());
        t.setMaxPeople(dto.getMaxPeople());
        t.setAvailableSlots(dto.getAvailableSlots());
        Tour saved = tourRepository.save(t);
        return toDto(saved);
    }

    @Transactional
    public Optional<TourResponseDto> update(Long id, TourUpdateDto dto) {
        return tourRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setDurationDays(dto.getDurationDays());
            existing.setMaxPeople(dto.getMaxPeople());
            existing.setAvailableSlots(dto.getAvailableSlots());
            Tour updated = tourRepository.save(existing);
            return toDto(updated);
        });
    }

    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

    private TourResponseDto toDto(Tour t) {
        TourResponseDto r = new TourResponseDto();
        r.setTourId(t.getTourId());
        r.setName(t.getName());
        r.setDescription(t.getDescription());
        if (t.getCategory() != null) {
            r.setCategoryId(t.getCategory().getCategoryId());
            r.setCategoryName(t.getCategory().getName());
        }
        if (t.getLocation() != null) {
            r.setLocationId(t.getLocation().getLocationId());
            r.setLocationName(t.getLocation().getName());
        }
        r.setDurationDays(t.getDurationDays());
        r.setMaxPeople(t.getMaxPeople());
        r.setAvailableSlots(t.getAvailableSlots());
        r.setStatus(t.getStatus() != null ? t.getStatus().name() : null);
        return r;
    }
}
