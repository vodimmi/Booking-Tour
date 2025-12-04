package com.example.tour.repository;

import com.example.tour.entity.TourTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourTimelineRepository extends JpaRepository<TourTimeline, Long> {
    List<TourTimeline> findByTourIdOrderByDayNumberAscStartTimeAsc(Long tourId);
}
