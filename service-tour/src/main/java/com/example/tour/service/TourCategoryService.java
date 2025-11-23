package com.example.tour.service;

import com.example.tour.entity.TourCategory;
import com.example.tour.repository.TourCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourCategoryService {

    private final TourCategoryRepository categoryRepository;

    public TourCategoryService(TourCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    public List<TourCategory> getAllCategories() {
//        return categoryRepository.findAll();
//    }

    public TourCategory getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<TourCategory> getAllCategories() {
        return categoryRepository.findByStatus(TourCategory.Status.ACTIVE);
    }

    public TourCategory createCategory(TourCategory cat) {
        cat.setStatus(TourCategory.Status.ACTIVE);
        return categoryRepository.save(cat);
    }

    public TourCategory updateCategory(Long id, TourCategory cat) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(cat.getName());
            existing.setDescription(cat.getDescription());
            return categoryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresent(cat -> {
            cat.setStatus(TourCategory.Status.INACTIVE);
            categoryRepository.save(cat);
        });
    }
}
