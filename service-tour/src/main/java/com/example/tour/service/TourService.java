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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Tour t = Tour.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .image(dto.getImage())
                .rating(dto.getRating())
                .reviewCount(dto.getReviewCount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .durationDays(dto.getDurationDays())
                .maxPeople(dto.getMaxPeople())
                .availableSlots(dto.getAvailableSlots())
                .category(category)
                .location(location)
                .build();

        return toDto(tourRepository.save(t));
    }

    @Transactional
    public Optional<TourResponseDto> update(Long id, TourUpdateDto dto) {
        return tourRepository.findById(id).map(existing -> {
            if (dto.getName() != null) existing.setName(dto.getName());
            if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
            if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
            if (dto.getImage() != null) existing.setImage(dto.getImage());
            if (dto.getRating() != null) existing.setRating(dto.getRating());
            if (dto.getReviewCount() != null) existing.setReviewCount(dto.getReviewCount());
            if (dto.getStartDate() != null) existing.setStartDate(dto.getStartDate());
            if (dto.getEndDate() != null) existing.setEndDate(dto.getEndDate());
            if (dto.getDurationDays() != null) existing.setDurationDays(dto.getDurationDays());
            if (dto.getMaxPeople() != null) existing.setMaxPeople(dto.getMaxPeople());
            if (dto.getAvailableSlots() != null) existing.setAvailableSlots(dto.getAvailableSlots());

            if (dto.getCategoryId() != null) {
                TourCategory category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategoryId()));
                existing.setCategory(category);
            }

            if (dto.getLocationId() != null) {
                Location location = locationRepository.findById(dto.getLocationId())
                        .orElseThrow(() -> new IllegalArgumentException("Location not found: " + dto.getLocationId()));
                existing.setLocation(location);
            }

            return toDto(tourRepository.save(existing));
        });
    }

    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

    private TourResponseDto toDto(Tour t) {
        TourResponseDto r = new TourResponseDto();
        r.setId(t.getTourId());
        r.setName(t.getName());
        r.setDescription(t.getDescription());
        r.setPrice(t.getPrice());
        r.setDuration(t.getDurationDays());
        r.setCategory(t.getCategory() != null ? t.getCategory().getName() : null);
        r.setImage(t.getImage());
        r.setRating(t.getRating());
        r.setReviews(t.getReviewCount());
        r.setLocation(t.getLocation() != null ? t.getLocation().getName() : null);
        r.setStartDate(t.getStartDate());
        r.setEndDate(t.getEndDate());
        r.setMaxParticipants(t.getMaxPeople());
        r.setCurrentParticipants(t.getAvailableSlots());
        return r;
    }

    public List<TourResponseDto> findByCategory(Long categoryId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Tour> tourPage = tourRepository.findByCategory_CategoryId(categoryId, pageable);
        return tourPage.getContent().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TourResponseDto> searchTours(String keyword) {
        List<Tour> tours = tourRepository.searchTours(keyword);
        return tours.stream().map(this::toDto).collect(Collectors.toList());
    }

}
