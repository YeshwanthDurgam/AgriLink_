package com.agrilink.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for ListingImage information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingImageDto {

    private UUID id;
    private String imageUrl;
    private boolean primary;
    private int sortOrder;
}
