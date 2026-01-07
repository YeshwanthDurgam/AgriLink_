package com.agrilink.marketplace.repository;

import com.agrilink.marketplace.entity.SavedListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for SavedListing entity.
 */
@Repository
public interface SavedListingRepository extends JpaRepository<SavedListing, UUID> {

    Page<SavedListing> findByUserId(UUID userId, Pageable pageable);

    Optional<SavedListing> findByUserIdAndListingId(UUID userId, UUID listingId);

    boolean existsByUserIdAndListingId(UUID userId, UUID listingId);

    void deleteByUserIdAndListingId(UUID userId, UUID listingId);

    long countByUserId(UUID userId);
}
