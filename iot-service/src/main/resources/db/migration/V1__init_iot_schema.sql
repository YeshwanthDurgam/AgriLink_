-- IoT Schema for AgriLink

-- Devices table
CREATE TABLE devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    farm_id UUID,
    field_id UUID,
    device_type VARCHAR(50) NOT NULL,
    device_name VARCHAR(100) NOT NULL,
    device_serial VARCHAR(100) UNIQUE,
    manufacturer VARCHAR(100),
    model VARCHAR(100),
    firmware_version VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    last_seen_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Telemetry data table (time-series data)
CREATE TABLE telemetry (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id UUID NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    metric_type VARCHAR(50) NOT NULL,
    metric_value DECIMAL(14,4) NOT NULL,
    unit VARCHAR(20),
    recorded_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alerts table
CREATE TABLE alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id UUID NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    farmer_id UUID NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    metric_type VARCHAR(50),
    metric_value DECIMAL(14,4),
    threshold_value DECIMAL(14,4),
    acknowledged BOOLEAN DEFAULT false,
    acknowledged_at TIMESTAMP,
    acknowledged_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alert rules table
CREATE TABLE alert_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
    metric_type VARCHAR(50) NOT NULL,
    condition VARCHAR(20) NOT NULL,
    threshold_value DECIMAL(14,4) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_devices_farmer ON devices(farmer_id);
CREATE INDEX idx_devices_farm ON devices(farm_id);
CREATE INDEX idx_devices_field ON devices(field_id);
CREATE INDEX idx_devices_status ON devices(status);
CREATE INDEX idx_telemetry_device ON telemetry(device_id);
CREATE INDEX idx_telemetry_device_time ON telemetry(device_id, recorded_at DESC);
CREATE INDEX idx_telemetry_metric_type ON telemetry(metric_type);
CREATE INDEX idx_alerts_farmer ON alerts(farmer_id);
CREATE INDEX idx_alerts_device ON alerts(device_id);
CREATE INDEX idx_alerts_acknowledged ON alerts(acknowledged);
CREATE INDEX idx_alert_rules_farmer ON alert_rules(farmer_id);
