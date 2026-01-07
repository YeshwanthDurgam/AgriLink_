package com.agrilink.farm.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.farm.dto.CreateCropPlanRequest;
import com.agrilink.farm.dto.CropPlanDto;
import com.agrilink.farm.entity.CropPlan;
import com.agrilink.farm.service.CropPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for crop plan operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CropPlanController {

    private final CropPlanService cropPlanService;

    /**
     * Create a new crop plan.
     * POST /api/v1/fields/{fieldId}/crop-plans
     */
    @PostMapping("/fields/{fieldId}/crop-plans")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<CropPlanDto>> createCropPlan(
            @PathVariable UUID fieldId,
            @Valid @RequestBody CreateCropPlanRequest request) {
        CropPlanDto cropPlan = cropPlanService.createCropPlan(fieldId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Crop plan created successfully", cropPlan));
    }

    /**
     * Get crop plans by field.
     * GET /api/v1/fields/{fieldId}/crop-plans
     */
    @GetMapping("/fields/{fieldId}/crop-plans")
    public ResponseEntity<ApiResponse<List<CropPlanDto>>> getCropPlansByField(@PathVariable UUID fieldId) {
        List<CropPlanDto> cropPlans = cropPlanService.getCropPlansByField(fieldId);
        return ResponseEntity.ok(ApiResponse.success(cropPlans));
    }

    /**
     * Get crop plans by farm.
     * GET /api/v1/farms/{farmId}/crop-plans
     */
    @GetMapping("/farms/{farmId}/crop-plans")
    public ResponseEntity<ApiResponse<List<CropPlanDto>>> getCropPlansByFarm(@PathVariable UUID farmId) {
        List<CropPlanDto> cropPlans = cropPlanService.getCropPlansByFarm(farmId);
        return ResponseEntity.ok(ApiResponse.success(cropPlans));
    }

    /**
     * Get crop plan by ID.
     * GET /api/v1/crop-plans/{cropPlanId}
     */
    @GetMapping("/crop-plans/{cropPlanId}")
    public ResponseEntity<ApiResponse<CropPlanDto>> getCropPlan(@PathVariable UUID cropPlanId) {
        CropPlanDto cropPlan = cropPlanService.getCropPlan(cropPlanId);
        return ResponseEntity.ok(ApiResponse.success(cropPlan));
    }

    /**
     * Update crop plan.
     * PUT /api/v1/crop-plans/{cropPlanId}
     */
    @PutMapping("/crop-plans/{cropPlanId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<CropPlanDto>> updateCropPlan(
            @PathVariable UUID cropPlanId,
            @Valid @RequestBody CropPlanDto request) {
        CropPlanDto cropPlan = cropPlanService.updateCropPlan(cropPlanId, request);
        return ResponseEntity.ok(ApiResponse.success("Crop plan updated successfully", cropPlan));
    }

    /**
     * Update crop plan status.
     * PATCH /api/v1/crop-plans/{cropPlanId}/status
     */
    @PatchMapping("/crop-plans/{cropPlanId}/status")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<CropPlanDto>> updateCropPlanStatus(
            @PathVariable UUID cropPlanId,
            @RequestParam CropPlan.CropStatus status) {
        CropPlanDto cropPlan = cropPlanService.updateCropPlanStatus(cropPlanId, status);
        return ResponseEntity.ok(ApiResponse.success("Crop plan status updated", cropPlan));
    }

    /**
     * Delete crop plan.
     * DELETE /api/v1/crop-plans/{cropPlanId}
     */
    @DeleteMapping("/crop-plans/{cropPlanId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Void>> deleteCropPlan(@PathVariable UUID cropPlanId) {
        cropPlanService.deleteCropPlan(cropPlanId);
        return ResponseEntity.ok(ApiResponse.success("Crop plan deleted successfully"));
    }
}
