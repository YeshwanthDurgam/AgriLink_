package com.agrilink.iot.repository;

import com.agrilink.iot.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Device entity.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByFarmerId(UUID farmerId);

    Page<Device> findByFarmerId(UUID farmerId, Pageable pageable);

    List<Device> findByFarmerIdAndStatus(UUID farmerId, Device.DeviceStatus status);

    List<Device> findByFarmId(UUID farmId);

    List<Device> findByFieldId(UUID fieldId);

    Optional<Device> findByDeviceSerial(String deviceSerial);

    List<Device> findByDeviceType(Device.DeviceType deviceType);

    long countByFarmerId(UUID farmerId);

    long countByFarmerIdAndStatus(UUID farmerId, Device.DeviceStatus status);
}
