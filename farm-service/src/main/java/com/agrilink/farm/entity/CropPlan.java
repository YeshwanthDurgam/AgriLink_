package com.agrilink.farm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CropPlan entity representing a planned or ongoing crop cultivation.
 */
@Entity
@Table(name = "crop_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(name = "crop_name", nullable = false)
    private String cropName;

    @Column
    private String variety;

    @Column(name = "planting_date")
    private LocalDate plantingDate;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "actual_harvest_date")
    private LocalDate actualHarvestDate;

    @Column(name = "expected_yield", precision = 10, scale = 2)
    private BigDecimal expectedYield;

    @Column(name = "actual_yield", precision = 10, scale = 2)
    private BigDecimal actualYield;

    @Column(name = "yield_unit", length = 20)
    @Builder.Default
    private String yieldUnit = "KG";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private CropStatus status = CropStatus.PLANNED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum CropStatus {
        PLANNED, PLANTED, GROWING, HARVESTING, HARVESTED, CANCELLED
    }
}
