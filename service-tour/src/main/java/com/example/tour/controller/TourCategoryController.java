package com.example.tour.controller;

import com.example.tour.entity.TourCategory;
import com.example.tour.service.TourCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours/categories")
public class TourCategoryController {

    private final TourCategoryService categoryService;

    public TourCategoryController(TourCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<TourCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public TourCategory createCategory(@RequestBody TourCategory cat) {
        return categoryService.createCategory(cat);
    }

    @PutMapping("/{id}")
    public TourCategory updateCategory(@PathVariable Long id, @RequestBody TourCategory cat) {
        return categoryService.updateCategory(id, cat);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
