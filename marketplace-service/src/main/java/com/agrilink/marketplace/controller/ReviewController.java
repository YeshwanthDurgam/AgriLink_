package com.agrilink.marketplace.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.common.dto.PagedResponse;
import com.agrilink.marketplace.dto.CreateReviewRequest;
import com.agrilink.marketplace.dto.ReviewDto;
import com.agrilink.marketplace.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for review operations.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Create a review for a listing.
     * POST /api/v1/listings/{listingId}/reviews
     */
    @PostMapping("/listings/{listingId}/reviews")
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            Authentication authentication,
            @PathVariable UUID listingId,
            @Valid @RequestBody CreateReviewRequest request) {
        UUID reviewerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        ReviewDto review = reviewService.createReview(listingId, reviewerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully", review));
    }

    /**
     * Get reviews for a listing.
     * GET /api/v1/listings/{listingId}/reviews
     */
    @GetMapping("/listings/{listingId}/reviews")
    public ResponseEntity<ApiResponse<PagedResponse<ReviewDto>>> getListingReviews(
            @PathVariable UUID listingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewDto> reviews = reviewService.getReviewsByListing(listingId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(reviews)));
    }

    /**
     * Get reviews for a seller.
     * GET /api/v1/sellers/{sellerId}/reviews
     */
    @GetMapping("/sellers/{sellerId}/reviews")
    public ResponseEntity<ApiResponse<PagedResponse<ReviewDto>>> getSellerReviews(
            @PathVariable UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewDto> reviews = reviewService.getReviewsBySeller(sellerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(reviews)));
    }

    /**
     * Get seller rating.
     * GET /api/v1/sellers/{sellerId}/rating
     */
    @GetMapping("/sellers/{sellerId}/rating")
    public ResponseEntity<ApiResponse<Double>> getSellerRating(@PathVariable UUID sellerId) {
        Double rating = reviewService.getSellerRating(sellerId);
        return ResponseEntity.ok(ApiResponse.success(rating != null ? rating : 0.0));
    }

    /**
     * Delete a review.
     * DELETE /api/v1/reviews/{reviewId}
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            Authentication authentication,
            @PathVariable UUID reviewId) {
        UUID reviewerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        reviewService.deleteReview(reviewId, reviewerId);
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully"));
    }
}
