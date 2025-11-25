package com.example.tour.repository;

import com.example.tour.entity.TourCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourCategoryRepository extends JpaRepository<TourCategory, Long> {
    List<TourCategory> findByStatus(TourCategory.Status status);
}
