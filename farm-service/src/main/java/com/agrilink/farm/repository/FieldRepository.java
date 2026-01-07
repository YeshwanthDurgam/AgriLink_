package com.agrilink.farm.repository;

import com.agrilink.farm.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Field entity operations.
 */
@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {
    
    List<Field> findByFarmIdAndActiveTrue(UUID farmId);
    
    List<Field> findByFarmFarmerIdAndActiveTrue(UUID farmerId);
}
