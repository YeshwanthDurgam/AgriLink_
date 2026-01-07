package com.agrilink.farm.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.farm.dto.CreateFarmRequest;
import com.agrilink.farm.dto.FarmDto;
import com.agrilink.farm.service.FarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for farm operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
public class FarmController {

    private final FarmService farmService;

    /**
     * Create a new farm.
     * POST /api/v1/farms
     */
    @PostMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<FarmDto>> createFarm(
            Authentication authentication,
            @Valid @RequestBody CreateFarmRequest request) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        FarmDto farm = farmService.createFarm(farmerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Farm created successfully", farm));
    }

    /**
     * Get all farms for current user.
     * GET /api/v1/farms
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FarmDto>>> getFarms(Authentication authentication) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        List<FarmDto> farms = farmService.getFarmsByFarmer(farmerId);
        return ResponseEntity.ok(ApiResponse.success(farms));
    }

    /**
     * Get farm by ID.
     * GET /api/v1/farms/{farmId}
     */
    @GetMapping("/{farmId}")
    public ResponseEntity<ApiResponse<FarmDto>> getFarm(
            Authentication authentication,
            @PathVariable UUID farmId) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        FarmDto farm = farmService.getFarm(farmId, farmerId);
        return ResponseEntity.ok(ApiResponse.success(farm));
    }

    /**
     * Update farm.
     * PUT /api/v1/farms/{farmId}
     */
    @PutMapping("/{farmId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<FarmDto>> updateFarm(
            Authentication authentication,
            @PathVariable UUID farmId,
            @Valid @RequestBody CreateFarmRequest request) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        FarmDto farm = farmService.updateFarm(farmId, farmerId, request);
        return ResponseEntity.ok(ApiResponse.success("Farm updated successfully", farm));
    }

    /**
     * Delete farm.
     * DELETE /api/v1/farms/{farmId}
     */
    @DeleteMapping("/{farmId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Void>> deleteFarm(
            Authentication authentication,
            @PathVariable UUID farmId) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        farmService.deleteFarm(farmId, farmerId);
        return ResponseEntity.ok(ApiResponse.success("Farm deleted successfully"));
    }
}
