package com.agrilink.marketplace.service;

import com.agrilink.common.exception.BadRequestException;
import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.marketplace.dto.CreateReviewRequest;
import com.agrilink.marketplace.dto.ReviewDto;
import com.agrilink.marketplace.entity.Listing;
import com.agrilink.marketplace.entity.Review;
import com.agrilink.marketplace.repository.ListingRepository;
import com.agrilink.marketplace.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for review operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ListingRepository listingRepository;

    /**
     * Create a review.
     */
    @Transactional
    public ReviewDto createReview(UUID listingId, UUID reviewerId, CreateReviewRequest request) {
        log.info("Creating review for listing: {} by user: {}", listingId, reviewerId);

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", "id", listingId));

        // Check if user already reviewed this listing
        if (reviewRepository.existsByListingIdAndReviewerId(listingId, reviewerId)) {
            throw new BadRequestException("You have already reviewed this listing");
        }

        // Cannot review own listing
        if (listing.getSellerId().equals(reviewerId)) {
            throw new BadRequestException("You cannot review your own listing");
        }

        Review review = Review.builder()
                .listing(listing)
                .reviewerId(reviewerId)
                .sellerId(listing.getSellerId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with id: {}", savedReview.getId());

        return mapToDto(savedReview);
    }

    /**
     * Get reviews for a listing.
     */
    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsByListing(UUID listingId, Pageable pageable) {
        return reviewRepository.findByListingId(listingId, pageable)
                .map(this::mapToDto);
    }

    /**
     * Get reviews for a seller.
     */
    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsBySeller(UUID sellerId, Pageable pageable) {
        return reviewRepository.findBySellerId(sellerId, pageable)
                .map(this::mapToDto);
    }

    /**
     * Get average rating for a seller.
     */
    @Transactional(readOnly = true)
    public Double getSellerRating(UUID sellerId) {
        return reviewRepository.getAverageRatingBySeller(sellerId);
    }

    /**
     * Delete a review.
     */
    @Transactional
    public void deleteReview(UUID reviewId, UUID reviewerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        if (!review.getReviewerId().equals(reviewerId)) {
            throw new BadRequestException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .listingId(review.getListing().getId())
                .reviewerId(review.getReviewerId())
                .sellerId(review.getSellerId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
