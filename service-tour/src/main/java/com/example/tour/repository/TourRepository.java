package com.example.tour.repository;

import com.example.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t WHERE t.status <> 'INACTIVE'")
    Page<Tour> findAllActive(Pageable pageable);

    // Chỉ lấy tour ACTIVE theo category
    @Query("SELECT t FROM Tour t WHERE t.category.categoryId = :categoryId AND t.status <> 'INACTIVE'")
    Page<Tour> findActiveByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    // Chỉ lấy tour ACTIVE theo từ khóa
    @Query("SELECT t FROM Tour t WHERE (LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND t.status <> 'INACTIVE'")
    Page<Tour> searchActiveToursPaged(@Param("keyword") String keyword, Pageable pageable);
}
