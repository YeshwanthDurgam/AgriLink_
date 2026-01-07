package com.agrilink.iot.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.common.dto.PagedResponse;
import com.agrilink.iot.dto.BatchTelemetryRequest;
import com.agrilink.iot.dto.TelemetryDto;
import com.agrilink.iot.dto.TelemetryIngestRequest;
import com.agrilink.iot.entity.Telemetry;
import com.agrilink.iot.service.TelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for telemetry operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    /**
     * Ingest telemetry data.
     * POST /api/v1/telemetry/ingest
     */
    @PostMapping("/ingest")
    public ResponseEntity<ApiResponse<TelemetryDto>> ingestTelemetry(
            @Valid @RequestBody TelemetryIngestRequest request) {
        TelemetryDto telemetry = telemetryService.ingestTelemetry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Telemetry ingested", telemetry));
    }

    /**
     * Batch ingest telemetry data.
     * POST /api/v1/telemetry/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<TelemetryDto>>> batchIngestTelemetry(
            @Valid @RequestBody BatchTelemetryRequest request) {
        List<TelemetryDto> telemetry = telemetryService.batchIngestTelemetry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Batch telemetry ingested", telemetry));
    }

    /**
     * Get telemetry by device.
     * GET /api/v1/telemetry/device/{deviceId}
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<ApiResponse<PagedResponse<TelemetryDto>>> getTelemetryByDevice(
            @PathVariable UUID deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("recordedAt").descending());
        Page<TelemetryDto> telemetry = telemetryService.getTelemetryByDevice(deviceId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(telemetry)));
    }

    /**
     * Get telemetry by device and time range.
     * GET /api/v1/telemetry/device/{deviceId}/range
     */
    @GetMapping("/device/{deviceId}/range")
    public ResponseEntity<ApiResponse<List<TelemetryDto>>> getTelemetryByTimeRange(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TelemetryDto> telemetry = telemetryService.getTelemetryByDeviceAndTimeRange(deviceId, start, end);
        return ResponseEntity.ok(ApiResponse.success(telemetry));
    }

    /**
     * Get telemetry by metric type.
     * GET /api/v1/telemetry/device/{deviceId}/metric/{metricType}
     */
    @GetMapping("/device/{deviceId}/metric/{metricType}")
    public ResponseEntity<ApiResponse<List<TelemetryDto>>> getTelemetryByMetricType(
            @PathVariable UUID deviceId,
            @PathVariable Telemetry.MetricType metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TelemetryDto> telemetry = telemetryService.getTelemetryByDeviceAndMetricType(deviceId, metricType, start, end);
        return ResponseEntity.ok(ApiResponse.success(telemetry));
    }

    /**
     * Get latest telemetry.
     * GET /api/v1/telemetry/device/{deviceId}/latest
     */
    @GetMapping("/device/{deviceId}/latest")
    public ResponseEntity<ApiResponse<List<TelemetryDto>>> getLatestTelemetry(
            @PathVariable UUID deviceId,
            @RequestParam(defaultValue = "10") int count) {
        List<TelemetryDto> telemetry = telemetryService.getLatestTelemetry(deviceId, count);
        return ResponseEntity.ok(ApiResponse.success(telemetry));
    }

    /**
     * Get telemetry statistics.
     * GET /api/v1/telemetry/device/{deviceId}/stats
     */
    @GetMapping("/device/{deviceId}/stats")
    public ResponseEntity<ApiResponse<TelemetryService.TelemetryStats>> getTelemetryStats(
            @PathVariable UUID deviceId,
            @RequestParam Telemetry.MetricType metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        TelemetryService.TelemetryStats stats = telemetryService.getStats(deviceId, metricType, start, end);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
