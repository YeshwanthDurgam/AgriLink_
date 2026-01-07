package com.agrilink.farm.service;

import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.farm.dto.CreateFieldRequest;
import com.agrilink.farm.dto.FieldDto;
import com.agrilink.farm.entity.Farm;
import com.agrilink.farm.entity.Field;
import com.agrilink.farm.repository.FarmRepository;
import com.agrilink.farm.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for field operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;

    /**
     * Create a new field.
     */
    @Transactional
    public FieldDto createField(UUID farmId, UUID farmerId, CreateFieldRequest request) {
        log.info("Creating field for farm: {}", farmId);

        Farm farm = farmRepository.findByIdAndFarmerId(farmId, farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", farmId));

        Field field = Field.builder()
                .farm(farm)
                .name(request.getName())
                .area(request.getArea())
                .areaUnit(request.getAreaUnit() != null ? request.getAreaUnit() : "HECTARE")
                .soilType(request.getSoilType())
                .irrigationType(request.getIrrigationType())
                .polygon(request.getPolygon())
                .active(true)
                .build();

        Field savedField = fieldRepository.save(field);
        log.info("Field created with id: {}", savedField.getId());

        return mapToDto(savedField);
    }

    /**
     * Get all fields for a farm.
     */
    @Transactional(readOnly = true)
    public List<FieldDto> getFieldsByFarm(UUID farmId) {
        log.info("Getting fields for farm: {}", farmId);
        return fieldRepository.findByFarmIdAndActiveTrue(farmId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get field by ID.
     */
    @Transactional(readOnly = true)
    public FieldDto getField(UUID fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field", "id", fieldId));
        return mapToDto(field);
    }

    /**
     * Update field.
     */
    @Transactional
    public FieldDto updateField(UUID fieldId, CreateFieldRequest request) {
        log.info("Updating field: {}", fieldId);

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field", "id", fieldId));

        if (request.getName() != null) field.setName(request.getName());
        if (request.getArea() != null) field.setArea(request.getArea());
        if (request.getAreaUnit() != null) field.setAreaUnit(request.getAreaUnit());
        if (request.getSoilType() != null) field.setSoilType(request.getSoilType());
        if (request.getIrrigationType() != null) field.setIrrigationType(request.getIrrigationType());
        if (request.getPolygon() != null) field.setPolygon(request.getPolygon());

        Field updatedField = fieldRepository.save(field);
        return mapToDto(updatedField);
    }

    /**
     * Delete (soft) field.
     */
    @Transactional
    public void deleteField(UUID fieldId) {
        log.info("Deleting field: {}", fieldId);

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field", "id", fieldId));

        field.setActive(false);
        fieldRepository.save(field);
    }

    private FieldDto mapToDto(Field field) {
        return FieldDto.builder()
                .id(field.getId())
                .farmId(field.getFarm().getId())
                .name(field.getName())
                .area(field.getArea())
                .areaUnit(field.getAreaUnit())
                .soilType(field.getSoilType())
                .irrigationType(field.getIrrigationType())
                .polygon(field.getPolygon())
                .active(field.isActive())
                .cropPlanCount(field.getCropPlans() != null ? field.getCropPlans().size() : 0)
                .createdAt(field.getCreatedAt())
                .updatedAt(field.getUpdatedAt())
                .build();
    }
}
