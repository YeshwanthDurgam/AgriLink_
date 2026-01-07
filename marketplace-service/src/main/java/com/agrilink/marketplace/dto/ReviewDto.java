package com.agrilink.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Review information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private UUID id;
    private UUID listingId;
    private UUID reviewerId;
    private UUID sellerId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
