package com.agrilink.farm.dto;

import com.agrilink.farm.entity.CropPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for CropPlan information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropPlanDto {

    private UUID id;

    @NotNull(message = "Field ID is required")
    private UUID fieldId;

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String variety;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
    private LocalDate actualHarvestDate;

    @Positive(message = "Expected yield must be positive")
    private BigDecimal expectedYield;

    @Positive(message = "Actual yield must be positive")
    private BigDecimal actualYield;

    private String yieldUnit;
    private CropPlan.CropStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
