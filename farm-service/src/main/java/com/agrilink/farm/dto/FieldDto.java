package com.agrilink.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for Field information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto {

    private UUID id;

    @NotNull(message = "Farm ID is required")
    private UUID farmId;

    @NotBlank(message = "Field name is required")
    private String name;

    @Positive(message = "Area must be positive")
    private BigDecimal area;

    private String areaUnit;
    private String soilType;
    private String irrigationType;
    private Map<String, Object> polygon;
    private boolean active;
    private int cropPlanCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
