package com.agrilink.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for listing search criteria.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingSearchCriteria {

    private String keyword;
    private UUID categoryId;
    private String cropType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String location;
    private Boolean organicOnly;
    private String qualityGrade;
    private UUID sellerId;
}
