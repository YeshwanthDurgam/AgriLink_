package com.agrilink.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating a new listing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateListingRequest {

    private UUID farmId;
    private UUID categoryId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String cropType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private String quantityUnit;

    @NotNull(message = "Price per unit is required")
    @Positive(message = "Price must be positive")
    private BigDecimal pricePerUnit;

    private String currency;
    private BigDecimal minimumOrder;
    private LocalDate harvestDate;
    private LocalDate expiryDate;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean organicCertified;
    private String qualityGrade;
    private List<String> imageUrls;
}
