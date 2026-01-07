package com.agrilink.marketplace.service;

import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.marketplace.dto.CategoryDto;
import com.agrilink.marketplace.entity.Category;
import com.agrilink.marketplace.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for category operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Get all root categories (no parent).
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> getRootCategories() {
        return categoryRepository.findByParentIsNullAndActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all categories.
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get category by ID.
     */
    @Transactional(readOnly = true)
    public CategoryDto getCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return mapToDto(category);
    }

    /**
     * Get subcategories.
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> getSubcategories(UUID parentId) {
        return categoryRepository.findByParentIdAndActiveTrue(parentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .active(category.isActive())
                .build();
    }
}
