package com.example.tour.config;

import com.example.tour.entity.Location;
import com.example.tour.entity.Tour;
import com.example.tour.entity.TourCategory;
import com.example.tour.repository.LocationRepository;
import com.example.tour.repository.TourCategoryRepository;
import com.example.tour.repository.TourRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TourCategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final TourRepository tourRepository;

    public DataInitializer(TourCategoryRepository categoryRepository,
                           LocationRepository locationRepository,
                           TourRepository tourRepository) {
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.tourRepository = tourRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            TourCategory c1 = new TourCategory();
            c1.setName("Du lịch biển");
            c1.setDescription("Các tour nghỉ dưỡng biển");
            categoryRepository.save(c1);

            TourCategory c2 = new TourCategory();
            c2.setName("Leo núi");
            c2.setDescription("Tour leo núi");
            categoryRepository.save(c2);

            Location l1 = new Location();
            l1.setName("Phú Quốc");
            l1.setCountry("Việt Nam");
            l1.setProvince("Kiên Giang");
            l1.setDescription("Đảo ngọc");
            locationRepository.save(l1);

            Location l2 = new Location();
            l2.setName("Sa Pa");
            l2.setCountry("Việt Nam");
            l2.setProvince("Lào Cai");
            l2.setDescription("Thị trấn mây");
            locationRepository.save(l2);

            Tour t1 = new Tour();
            t1.setName("Tour Phú Quốc 3N2Đ");
            t1.setDescription("Nghỉ dưỡng và lặn biển");
            t1.setCategory(c1);
            t1.setLocation(l1);
            t1.setDurationDays(3);
            t1.setMaxPeople(30);
            t1.setAvailableSlots(25);
            tourRepository.save(t1);

            Tour t2 = new Tour();
            t2.setName("Tour Sa Pa 2N1Đ");
            t2.setDescription("Leo núi và săn mây");
            t2.setCategory(c2);
            t2.setLocation(l2);
            t2.setDurationDays(2);
            t2.setMaxPeople(20);
            t2.setAvailableSlots(12);
            tourRepository.save(t2);
        }
    }
}
