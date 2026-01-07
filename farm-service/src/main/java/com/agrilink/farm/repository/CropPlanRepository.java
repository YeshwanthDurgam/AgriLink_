package com.agrilink.farm.repository;

import com.agrilink.farm.entity.CropPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for CropPlan entity operations.
 */
@Repository
public interface CropPlanRepository extends JpaRepository<CropPlan, UUID> {
    
    List<CropPlan> findByFieldId(UUID fieldId);
    
    List<CropPlan> findByStatus(CropPlan.CropStatus status);
    
    @Query("SELECT cp FROM CropPlan cp WHERE cp.field.farm.farmerId = :farmerId")
    List<CropPlan> findByFarmerId(@Param("farmerId") UUID farmerId);
    
    @Query("SELECT cp FROM CropPlan cp WHERE cp.field.farm.id = :farmId")
    List<CropPlan> findByFarmId(@Param("farmId") UUID farmId);
}
