package com.agrilink.marketplace.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.marketplace.dto.CategoryDto;
import com.agrilink.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for category operations.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Get all categories.
     * GET /api/v1/categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get root categories.
     * GET /api/v1/categories/root
     */
    @GetMapping("/root")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getRootCategories() {
        List<CategoryDto> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get category by ID.
     * GET /api/v1/categories/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(@PathVariable UUID categoryId) {
        CategoryDto category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    /**
     * Get subcategories.
     * GET /api/v1/categories/{categoryId}/subcategories
     */
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getSubcategories(@PathVariable UUID categoryId) {
        List<CategoryDto> subcategories = categoryService.getSubcategories(categoryId);
        return ResponseEntity.ok(ApiResponse.success(subcategories));
    }
}
