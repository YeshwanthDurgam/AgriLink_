package com.agrilink.farm.service;

import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.farm.dto.CreateCropPlanRequest;
import com.agrilink.farm.dto.CropPlanDto;
import com.agrilink.farm.entity.CropPlan;
import com.agrilink.farm.entity.Field;
import com.agrilink.farm.repository.CropPlanRepository;
import com.agrilink.farm.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for crop plan operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CropPlanService {

    private final CropPlanRepository cropPlanRepository;
    private final FieldRepository fieldRepository;

    /**
     * Create a new crop plan.
     */
    @Transactional
    public CropPlanDto createCropPlan(UUID fieldId, CreateCropPlanRequest request) {
        log.info("Creating crop plan for field: {}", fieldId);

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field", "id", fieldId));

        CropPlan cropPlan = CropPlan.builder()
                .field(field)
                .cropName(request.getCropName())
                .variety(request.getVariety())
                .plantingDate(request.getPlantingDate())
                .expectedHarvestDate(request.getExpectedHarvestDate())
                .expectedYield(request.getExpectedYield())
                .yieldUnit(request.getYieldUnit() != null ? request.getYieldUnit() : "KG")
                .notes(request.getNotes())
                .status(CropPlan.CropStatus.PLANNED)
                .build();

        CropPlan savedCropPlan = cropPlanRepository.save(cropPlan);
        log.info("Crop plan created with id: {}", savedCropPlan.getId());

        return mapToDto(savedCropPlan);
    }

    /**
     * Get crop plans by field.
     */
    @Transactional(readOnly = true)
    public List<CropPlanDto> getCropPlansByField(UUID fieldId) {
        return cropPlanRepository.findByFieldId(fieldId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get crop plans by farm.
     */
    @Transactional(readOnly = true)
    public List<CropPlanDto> getCropPlansByFarm(UUID farmId) {
        return cropPlanRepository.findByFarmId(farmId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get crop plan by ID.
     */
    @Transactional(readOnly = true)
    public CropPlanDto getCropPlan(UUID cropPlanId) {
        CropPlan cropPlan = cropPlanRepository.findById(cropPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("CropPlan", "id", cropPlanId));
        return mapToDto(cropPlan);
    }

    /**
     * Update crop plan status.
     */
    @Transactional
    public CropPlanDto updateCropPlanStatus(UUID cropPlanId, CropPlan.CropStatus status) {
        log.info("Updating crop plan status: {} to {}", cropPlanId, status);

        CropPlan cropPlan = cropPlanRepository.findById(cropPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("CropPlan", "id", cropPlanId));

        cropPlan.setStatus(status);
        CropPlan updatedCropPlan = cropPlanRepository.save(cropPlan);

        return mapToDto(updatedCropPlan);
    }

    /**
     * Update crop plan.
     */
    @Transactional
    public CropPlanDto updateCropPlan(UUID cropPlanId, CropPlanDto request) {
        log.info("Updating crop plan: {}", cropPlanId);

        CropPlan cropPlan = cropPlanRepository.findById(cropPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("CropPlan", "id", cropPlanId));

        if (request.getCropName() != null) cropPlan.setCropName(request.getCropName());
        if (request.getVariety() != null) cropPlan.setVariety(request.getVariety());
        if (request.getPlantingDate() != null) cropPlan.setPlantingDate(request.getPlantingDate());
        if (request.getExpectedHarvestDate() != null) cropPlan.setExpectedHarvestDate(request.getExpectedHarvestDate());
        if (request.getActualHarvestDate() != null) cropPlan.setActualHarvestDate(request.getActualHarvestDate());
        if (request.getExpectedYield() != null) cropPlan.setExpectedYield(request.getExpectedYield());
        if (request.getActualYield() != null) cropPlan.setActualYield(request.getActualYield());
        if (request.getYieldUnit() != null) cropPlan.setYieldUnit(request.getYieldUnit());
        if (request.getStatus() != null) cropPlan.setStatus(request.getStatus());
        if (request.getNotes() != null) cropPlan.setNotes(request.getNotes());

        CropPlan updatedCropPlan = cropPlanRepository.save(cropPlan);
        return mapToDto(updatedCropPlan);
    }

    /**
     * Delete crop plan.
     */
    @Transactional
    public void deleteCropPlan(UUID cropPlanId) {
        log.info("Deleting crop plan: {}", cropPlanId);

        if (!cropPlanRepository.existsById(cropPlanId)) {
            throw new ResourceNotFoundException("CropPlan", "id", cropPlanId);
        }

        cropPlanRepository.deleteById(cropPlanId);
    }

    private CropPlanDto mapToDto(CropPlan cropPlan) {
        return CropPlanDto.builder()
                .id(cropPlan.getId())
                .fieldId(cropPlan.getField().getId())
                .cropName(cropPlan.getCropName())
                .variety(cropPlan.getVariety())
                .plantingDate(cropPlan.getPlantingDate())
                .expectedHarvestDate(cropPlan.getExpectedHarvestDate())
                .actualHarvestDate(cropPlan.getActualHarvestDate())
                .expectedYield(cropPlan.getExpectedYield())
                .actualYield(cropPlan.getActualYield())
                .yieldUnit(cropPlan.getYieldUnit())
                .status(cropPlan.getStatus())
                .notes(cropPlan.getNotes())
                .createdAt(cropPlan.getCreatedAt())
                .updatedAt(cropPlan.getUpdatedAt())
                .build();
    }
}
