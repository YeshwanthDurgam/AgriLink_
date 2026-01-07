package com.agrilink.iot.repository;

import com.agrilink.iot.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Alert entity.
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

    Page<Alert> findByFarmerId(UUID farmerId, Pageable pageable);

    List<Alert> findByFarmerIdAndAcknowledgedFalse(UUID farmerId);

    Page<Alert> findByFarmerIdAndAcknowledged(UUID farmerId, boolean acknowledged, Pageable pageable);

    List<Alert> findByDeviceId(UUID deviceId);

    Page<Alert> findByDeviceId(UUID deviceId, Pageable pageable);

    List<Alert> findByFarmerIdAndSeverity(UUID farmerId, Alert.Severity severity);

    long countByFarmerIdAndAcknowledgedFalse(UUID farmerId);
}
