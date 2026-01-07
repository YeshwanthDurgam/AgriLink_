-- Marketplace Schema for AgriLink

-- Categories table
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    parent_id UUID REFERENCES categories(id),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Listings table
CREATE TABLE listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL,
    farm_id UUID,
    category_id UUID REFERENCES categories(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    crop_type VARCHAR(100),
    quantity DECIMAL(12,2) NOT NULL,
    quantity_unit VARCHAR(20) DEFAULT 'KG',
    price_per_unit DECIMAL(12,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    minimum_order DECIMAL(12,2),
    harvest_date DATE,
    expiry_date DATE,
    location VARCHAR(500),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    organic_certified BOOLEAN DEFAULT false,
    quality_grade VARCHAR(20),
    status VARCHAR(20) DEFAULT 'DRAFT',
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Listing images table
CREATE TABLE listing_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT false,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews table
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    reviewer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Saved listings (favorites)
CREATE TABLE saved_listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, listing_id)
);

-- Indexes
CREATE INDEX idx_listings_seller ON listings(seller_id);
CREATE INDEX idx_listings_category ON listings(category_id);
CREATE INDEX idx_listings_status ON listings(status);
CREATE INDEX idx_listings_crop_type ON listings(crop_type);
CREATE INDEX idx_listings_location ON listings(latitude, longitude);
CREATE INDEX idx_listings_price ON listings(price_per_unit);
CREATE INDEX idx_reviews_listing ON reviews(listing_id);
CREATE INDEX idx_reviews_seller ON reviews(seller_id);
CREATE INDEX idx_saved_listings_user ON saved_listings(user_id);

-- Insert default categories
INSERT INTO categories (name, description) VALUES
    ('Grains', 'Wheat, rice, corn, and other grains'),
    ('Vegetables', 'Fresh vegetables'),
    ('Fruits', 'Fresh fruits'),
    ('Dairy', 'Milk, cheese, and dairy products'),
    ('Livestock', 'Cattle, poultry, and other livestock'),
    ('Seeds', 'Planting seeds'),
    ('Fertilizers', 'Organic and chemical fertilizers'),
    ('Equipment', 'Farm equipment and tools');
