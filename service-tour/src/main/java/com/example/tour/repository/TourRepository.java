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
    Page<Tour> findByCategory_CategoryId(Long categoryId, Pageable pageable);
    List<Tour> findByCategory_CategoryId(Long categoryId);

    @Query("SELECT t FROM Tour t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Tour> searchTours(@Param("keyword") String keyword);
}
