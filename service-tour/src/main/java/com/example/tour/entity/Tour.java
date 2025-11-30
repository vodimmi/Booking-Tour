package com.example.tour.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price; // 💰 Giá tour

    private String image; // 🖼️ Ảnh đại diện

    private Double rating = 0.0; // ⭐ Đánh giá trung bình

    private Integer reviewCount = 0; // 💬 Số lượng đánh giá

    private LocalDate startDate; // 📅 Ngày bắt đầu tour
    private LocalDate endDate;   // 📅 Ngày kết thúc tour

    private Integer durationDays = 1;
    private Integer maxPeople = 20;
    private Integer availableSlots = 0;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TourCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE, INACTIVE, DRAFT
    }
}
