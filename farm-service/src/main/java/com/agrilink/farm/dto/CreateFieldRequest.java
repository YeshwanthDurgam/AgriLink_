package com.agrilink.farm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for creating a new field.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFieldRequest {

    @NotBlank(message = "Field name is required")
    private String name;

    @Positive(message = "Area must be positive")
    private BigDecimal area;

    private String areaUnit;
    private String soilType;
    private String irrigationType;
    private Map<String, Object> polygon;
}
