package com.agrilink.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new crop plan.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCropPlanRequest {

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String variety;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;

    @Positive(message = "Expected yield must be positive")
    private BigDecimal expectedYield;

    private String yieldUnit;
    private String notes;
}
