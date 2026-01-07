package com.agrilink.iot.repository;

import com.agrilink.iot.entity.AlertRule;
import com.agrilink.iot.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for AlertRule entity.
 */
@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {

    List<AlertRule> findByFarmerId(UUID farmerId);

    List<AlertRule> findByFarmerIdAndEnabledTrue(UUID farmerId);

    List<AlertRule> findByDeviceIdAndEnabledTrue(UUID deviceId);

    List<AlertRule> findByDeviceIdAndMetricTypeAndEnabledTrue(UUID deviceId, Telemetry.MetricType metricType);
}
