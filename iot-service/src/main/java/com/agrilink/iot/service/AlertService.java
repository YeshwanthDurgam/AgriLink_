package com.agrilink.iot.service;

import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.iot.dto.AlertDto;
import com.agrilink.iot.entity.Alert;
import com.agrilink.iot.entity.AlertRule;
import com.agrilink.iot.entity.Device;
import com.agrilink.iot.entity.Telemetry;
import com.agrilink.iot.repository.AlertRepository;
import com.agrilink.iot.repository.AlertRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for alert operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;

    /**
     * Check alert rules against incoming telemetry.
     */
    @Transactional
    public void checkAlertRules(Device device, Telemetry.MetricType metricType, BigDecimal metricValue) {
        List<AlertRule> rules = alertRuleRepository.findByDeviceIdAndMetricTypeAndEnabledTrue(device.getId(), metricType);

        for (AlertRule rule : rules) {
            if (shouldTriggerAlert(rule, metricValue)) {
                createAlert(device, rule, metricValue);
            }
        }
    }

    /**
     * Get alerts by farmer.
     */
    @Transactional(readOnly = true)
    public Page<AlertDto> getAlertsByFarmer(UUID farmerId, Pageable pageable) {
        return alertRepository.findByFarmerId(farmerId, pageable).map(this::mapToDto);
    }

    /**
     * Get unacknowledged alerts.
     */
    @Transactional(readOnly = true)
    public List<AlertDto> getUnacknowledgedAlerts(UUID farmerId) {
        return alertRepository.findByFarmerIdAndAcknowledgedFalse(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get alert count.
     */
    @Transactional(readOnly = true)
    public long getUnacknowledgedAlertCount(UUID farmerId) {
        return alertRepository.countByFarmerIdAndAcknowledgedFalse(farmerId);
    }

    /**
     * Acknowledge alert.
     */
    @Transactional
    public AlertDto acknowledgeAlert(UUID alertId, UUID userId) {
        log.info("Acknowledging alert: {}", alertId);

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", alertId));

        alert.setAcknowledged(true);
        alert.setAcknowledgedAt(LocalDateTime.now());
        alert.setAcknowledgedBy(userId);

        Alert updatedAlert = alertRepository.save(alert);
        return mapToDto(updatedAlert);
    }

    /**
     * Get alerts by device.
     */
    @Transactional(readOnly = true)
    public Page<AlertDto> getAlertsByDevice(UUID deviceId, Pageable pageable) {
        return alertRepository.findByDeviceId(deviceId, pageable).map(this::mapToDto);
    }

    private boolean shouldTriggerAlert(AlertRule rule, BigDecimal metricValue) {
        int comparison = metricValue.compareTo(rule.getThresholdValue());

        return switch (rule.getCondition()) {
            case GREATER_THAN -> comparison > 0;
            case LESS_THAN -> comparison < 0;
            case EQUALS -> comparison == 0;
            case NOT_EQUALS -> comparison != 0;
            case GREATER_OR_EQUAL -> comparison >= 0;
            case LESS_OR_EQUAL -> comparison <= 0;
        };
    }

    private void createAlert(Device device, AlertRule rule, BigDecimal metricValue) {
        String message = String.format("%s value %.2f %s threshold %.2f on device %s",
                rule.getMetricType(),
                metricValue,
                rule.getCondition().name().toLowerCase().replace("_", " "),
                rule.getThresholdValue(),
                device.getDeviceName());

        Alert alert = Alert.builder()
                .device(device)
                .farmerId(device.getFarmerId())
                .alertType(metricValue.compareTo(rule.getThresholdValue()) > 0 
                        ? Alert.AlertType.THRESHOLD_EXCEEDED 
                        : Alert.AlertType.THRESHOLD_BELOW)
                .severity(rule.getSeverity())
                .message(message)
                .metricType(rule.getMetricType())
                .metricValue(metricValue)
                .thresholdValue(rule.getThresholdValue())
                .acknowledged(false)
                .build();

        alertRepository.save(alert);
        log.warn("Alert created: {}", message);
    }

    private AlertDto mapToDto(Alert alert) {
        return AlertDto.builder()
                .id(alert.getId())
                .deviceId(alert.getDevice().getId())
                .deviceName(alert.getDevice().getDeviceName())
                .farmerId(alert.getFarmerId())
                .alertType(alert.getAlertType())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .metricType(alert.getMetricType())
                .metricValue(alert.getMetricValue())
                .thresholdValue(alert.getThresholdValue())
                .acknowledged(alert.isAcknowledged())
                .acknowledgedAt(alert.getAcknowledgedAt())
                .acknowledgedBy(alert.getAcknowledgedBy())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
