package com.example.tour.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
