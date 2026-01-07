package com.agrilink.iot.service;

import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.iot.dto.BatchTelemetryRequest;
import com.agrilink.iot.dto.TelemetryDto;
import com.agrilink.iot.dto.TelemetryIngestRequest;
import com.agrilink.iot.entity.Device;
import com.agrilink.iot.entity.Telemetry;
import com.agrilink.iot.repository.DeviceRepository;
import com.agrilink.iot.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for telemetry data operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceService deviceService;
    private final AlertService alertService;

    /**
     * Ingest telemetry data from a device.
     */
    @Transactional
    public TelemetryDto ingestTelemetry(TelemetryIngestRequest request) {
        log.debug("Ingesting telemetry from device: {}", request.getDeviceId());

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", request.getDeviceId()));

        Telemetry telemetry = Telemetry.builder()
                .device(device)
                .metricType(request.getMetricType())
                .metricValue(request.getMetricValue())
                .unit(request.getUnit())
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .build();

        Telemetry savedTelemetry = telemetryRepository.save(telemetry);

        // Update device last seen
        deviceService.updateLastSeen(device.getId());

        // Check alert rules
        alertService.checkAlertRules(device, request.getMetricType(), request.getMetricValue());

        return mapToDto(savedTelemetry);
    }

    /**
     * Batch ingest telemetry data.
     */
    @Transactional
    public List<TelemetryDto> batchIngestTelemetry(BatchTelemetryRequest request) {
        log.info("Batch ingesting {} readings from device: {}", request.getReadings().size(), request.getDeviceId());

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", request.getDeviceId()));

        List<Telemetry> telemetryList = request.getReadings().stream()
                .map(reading -> Telemetry.builder()
                        .device(device)
                        .metricType(reading.getMetricType())
                        .metricValue(reading.getMetricValue())
                        .unit(reading.getUnit())
                        .recordedAt(reading.getRecordedAt() != null ? reading.getRecordedAt() : LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        List<Telemetry> savedTelemetry = telemetryRepository.saveAll(telemetryList);

        // Update device last seen
        deviceService.updateLastSeen(device.getId());

        // Check alert rules for the latest reading of each metric type
        request.getReadings().forEach(reading -> 
                alertService.checkAlertRules(device, reading.getMetricType(), reading.getMetricValue()));

        return savedTelemetry.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get telemetry data for a device.
     */
    @Transactional(readOnly = true)
    public Page<TelemetryDto> getTelemetryByDevice(UUID deviceId, Pageable pageable) {
        return telemetryRepository.findByDeviceId(deviceId, pageable).map(this::mapToDto);
    }

    /**
     * Get telemetry data for a device within time range.
     */
    @Transactional(readOnly = true)
    public List<TelemetryDto> getTelemetryByDeviceAndTimeRange(UUID deviceId, LocalDateTime start, LocalDateTime end) {
        return telemetryRepository.findByDeviceIdAndTimeRange(deviceId, start, end).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get telemetry data by metric type.
     */
    @Transactional(readOnly = true)
    public List<TelemetryDto> getTelemetryByDeviceAndMetricType(UUID deviceId, Telemetry.MetricType metricType, 
                                                                  LocalDateTime start, LocalDateTime end) {
        return telemetryRepository.findByDeviceIdAndMetricTypeAndTimeRange(deviceId, metricType, start, end).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get latest telemetry for a device.
     */
    @Transactional(readOnly = true)
    public List<TelemetryDto> getLatestTelemetry(UUID deviceId, int count) {
        return telemetryRepository.findLatestByDeviceId(deviceId, PageRequest.of(0, count)).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get statistics for a metric.
     */
    @Transactional(readOnly = true)
    public TelemetryStats getStats(UUID deviceId, Telemetry.MetricType metricType, 
                                    LocalDateTime start, LocalDateTime end) {
        Double avg = telemetryRepository.getAverageMetricValue(deviceId, metricType, start, end);
        Double max = telemetryRepository.getMaxMetricValue(deviceId, metricType, start, end);
        Double min = telemetryRepository.getMinMetricValue(deviceId, metricType, start, end);

        return TelemetryStats.builder()
                .deviceId(deviceId)
                .metricType(metricType)
                .average(avg)
                .maximum(max)
                .minimum(min)
                .startTime(start)
                .endTime(end)
                .build();
    }

    private TelemetryDto mapToDto(Telemetry telemetry) {
        return TelemetryDto.builder()
                .id(telemetry.getId())
                .deviceId(telemetry.getDevice().getId())
                .metricType(telemetry.getMetricType())
                .metricValue(telemetry.getMetricValue())
                .unit(telemetry.getUnit())
                .recordedAt(telemetry.getRecordedAt())
                .build();
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TelemetryStats {
        private UUID deviceId;
        private Telemetry.MetricType metricType;
        private Double average;
        private Double maximum;
        private Double minimum;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
}
