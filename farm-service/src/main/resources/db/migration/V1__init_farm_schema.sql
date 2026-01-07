-- Farm Service Database Schema
-- V1: Farms, Fields, and Crop Plans

-- Create farms table
CREATE TABLE farms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(500),
    total_area DECIMAL(10, 2),
    area_unit VARCHAR(20) DEFAULT 'HECTARE',
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create fields table
CREATE TABLE fields (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farm_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    area DECIMAL(10, 2),
    area_unit VARCHAR(20) DEFAULT 'HECTARE',
    soil_type VARCHAR(100),
    irrigation_type VARCHAR(100),
    polygon JSONB,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (farm_id) REFERENCES farms(id) ON DELETE CASCADE
);

-- Create crop_plans table
CREATE TABLE crop_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    field_id UUID NOT NULL,
    crop_name VARCHAR(255) NOT NULL,
    variety VARCHAR(255),
    planting_date DATE,
    expected_harvest_date DATE,
    actual_harvest_date DATE,
    expected_yield DECIMAL(10, 2),
    actual_yield DECIMAL(10, 2),
    yield_unit VARCHAR(20) DEFAULT 'KG',
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_farms_farmer_id ON farms(farmer_id);
CREATE INDEX idx_farms_active ON farms(active);
CREATE INDEX idx_fields_farm_id ON fields(farm_id);
CREATE INDEX idx_fields_active ON fields(active);
CREATE INDEX idx_crop_plans_field_id ON crop_plans(field_id);
CREATE INDEX idx_crop_plans_status ON crop_plans(status);
CREATE INDEX idx_crop_plans_planting_date ON crop_plans(planting_date);
