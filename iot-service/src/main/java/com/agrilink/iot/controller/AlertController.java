package com.agrilink.iot.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.common.dto.PagedResponse;
import com.agrilink.iot.dto.AlertDto;
import com.agrilink.iot.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for alert operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * Get my alerts.
     * GET /api/v1/alerts
     */
    @GetMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getMyAlerts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AlertDto> alerts = alertService.getAlertsByFarmer(farmerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(alerts)));
    }

    /**
     * Get unacknowledged alerts.
     * GET /api/v1/alerts/unacknowledged
     */
    @GetMapping("/unacknowledged")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<List<AlertDto>>> getUnacknowledgedAlerts(Authentication authentication) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        List<AlertDto> alerts = alertService.getUnacknowledgedAlerts(farmerId);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    /**
     * Get unacknowledged alert count.
     * GET /api/v1/alerts/count
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Long>> getAlertCount(Authentication authentication) {
        UUID farmerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        long count = alertService.getUnacknowledgedAlertCount(farmerId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * Get alerts by device.
     * GET /api/v1/alerts/device/{deviceId}
     */
    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<PagedResponse<AlertDto>>> getAlertsByDevice(
            @PathVariable UUID deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AlertDto> alerts = alertService.getAlertsByDevice(deviceId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(alerts)));
    }

    /**
     * Acknowledge alert.
     * POST /api/v1/alerts/{alertId}/acknowledge
     */
    @PostMapping("/{alertId}/acknowledge")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<AlertDto>> acknowledgeAlert(
            Authentication authentication,
            @PathVariable UUID alertId) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        AlertDto alert = alertService.acknowledgeAlert(alertId, userId);
        return ResponseEntity.ok(ApiResponse.success("Alert acknowledged", alert));
    }
}
