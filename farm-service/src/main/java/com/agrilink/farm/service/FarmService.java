package com.agrilink.farm.service;

import com.agrilink.common.exception.ForbiddenException;
import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.farm.dto.CreateFarmRequest;
import com.agrilink.farm.dto.FarmDto;
import com.agrilink.farm.entity.Farm;
import com.agrilink.farm.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for farm operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository farmRepository;

    /**
     * Create a new farm.
     */
    @Transactional
    public FarmDto createFarm(UUID farmerId, CreateFarmRequest request) {
        log.info("Creating farm for farmer: {}", farmerId);

        Farm farm = Farm.builder()
                .farmerId(farmerId)
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .totalArea(request.getTotalArea())
                .areaUnit(request.getAreaUnit() != null ? request.getAreaUnit() : "HECTARE")
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .active(true)
                .build();

        Farm savedFarm = farmRepository.save(farm);
        log.info("Farm created with id: {}", savedFarm.getId());

        return mapToDto(savedFarm);
    }

    /**
     * Get all farms for a farmer.
     */
    @Transactional(readOnly = true)
    public List<FarmDto> getFarmsByFarmer(UUID farmerId) {
        log.info("Getting farms for farmer: {}", farmerId);
        return farmRepository.findByFarmerIdAndActiveTrue(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get farms with pagination.
     */
    @Transactional(readOnly = true)
    public Page<FarmDto> getFarmsByFarmer(UUID farmerId, Pageable pageable) {
        return farmRepository.findByFarmerIdAndActiveTrue(farmerId, pageable)
                .map(this::mapToDto);
    }

    /**
     * Get farm by ID.
     */
    @Transactional(readOnly = true)
    public FarmDto getFarm(UUID farmId, UUID farmerId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", farmId));

        if (!farm.getFarmerId().equals(farmerId)) {
            throw new ForbiddenException("You don't have access to this farm");
        }

        return mapToDto(farm);
    }

    /**
     * Update farm.
     */
    @Transactional
    public FarmDto updateFarm(UUID farmId, UUID farmerId, CreateFarmRequest request) {
        log.info("Updating farm: {}", farmId);

        Farm farm = farmRepository.findByIdAndFarmerId(farmId, farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", farmId));

        if (request.getName() != null) farm.setName(request.getName());
        if (request.getDescription() != null) farm.setDescription(request.getDescription());
        if (request.getLocation() != null) farm.setLocation(request.getLocation());
        if (request.getTotalArea() != null) farm.setTotalArea(request.getTotalArea());
        if (request.getAreaUnit() != null) farm.setAreaUnit(request.getAreaUnit());
        if (request.getLatitude() != null) farm.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) farm.setLongitude(request.getLongitude());

        Farm updatedFarm = farmRepository.save(farm);
        return mapToDto(updatedFarm);
    }

    /**
     * Delete (soft) farm.
     */
    @Transactional
    public void deleteFarm(UUID farmId, UUID farmerId) {
        log.info("Deleting farm: {}", farmId);

        Farm farm = farmRepository.findByIdAndFarmerId(farmId, farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm", "id", farmId));

        farm.setActive(false);
        farmRepository.save(farm);
    }

    private FarmDto mapToDto(Farm farm) {
        return FarmDto.builder()
                .id(farm.getId())
                .farmerId(farm.getFarmerId())
                .name(farm.getName())
                .description(farm.getDescription())
                .location(farm.getLocation())
                .totalArea(farm.getTotalArea())
                .areaUnit(farm.getAreaUnit())
                .latitude(farm.getLatitude())
                .longitude(farm.getLongitude())
                .active(farm.isActive())
                .fieldCount(farm.getFields() != null ? farm.getFields().size() : 0)
                .createdAt(farm.getCreatedAt())
                .updatedAt(farm.getUpdatedAt())
                .build();
    }
}
