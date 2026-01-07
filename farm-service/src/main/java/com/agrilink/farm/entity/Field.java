package com.agrilink.farm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Field entity representing a field within a farm.
 * Includes polygon stored as JSONB for geo boundaries.
 */
@Entity
@Table(name = "fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "area_unit", length = 20)
    @Builder.Default
    private String areaUnit = "HECTARE";

    @Column(name = "soil_type", length = 100)
    private String soilType;

    @Column(name = "irrigation_type", length = 100)
    private String irrigationType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> polygon;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CropPlan> cropPlans = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum SoilType {
        CLAY, SANDY, LOAMY, SILTY, PEATY, CHALKY, SALINE
    }

    public enum IrrigationType {
        DRIP, SPRINKLER, FLOOD, FURROW, CENTER_PIVOT, RAINFED
    }
}
