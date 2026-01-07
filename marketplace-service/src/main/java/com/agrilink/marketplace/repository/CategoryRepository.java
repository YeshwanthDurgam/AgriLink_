package com.agrilink.marketplace.repository;

import com.agrilink.marketplace.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);

    List<Category> findByActiveTrue();

    List<Category> findByParentIsNullAndActiveTrue();

    List<Category> findByParentIdAndActiveTrue(UUID parentId);

    boolean existsByName(String name);
}
