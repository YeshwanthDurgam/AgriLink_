-- Notification Service Schema

-- Notifications table
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    data JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    sent_at TIMESTAMP,
    failed_at TIMESTAMP,
    failure_reason TEXT,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Notification templates table
CREATE TABLE notification_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_code VARCHAR(100) NOT NULL UNIQUE,
    notification_type VARCHAR(50) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    title_template VARCHAR(255) NOT NULL,
    body_template TEXT NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Notification preferences table
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    email_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sms_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    push_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    order_updates BOOLEAN NOT NULL DEFAULT TRUE,
    listing_updates BOOLEAN NOT NULL DEFAULT TRUE,
    price_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    weather_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    iot_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    marketing BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_user_status ON notifications(user_id, status);
CREATE INDEX idx_notifications_user_read ON notifications(user_id, read);
CREATE INDEX idx_notifications_type ON notifications(notification_type);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);
CREATE INDEX idx_notification_templates_code ON notification_templates(template_code);
CREATE INDEX idx_notification_preferences_user ON notification_preferences(user_id);

-- Insert default templates
INSERT INTO notification_templates (template_code, notification_type, channel, title_template, body_template, description) VALUES
    ('ORDER_CREATED', 'ORDER', 'EMAIL', 'Order Confirmation - #{orderNumber}', 'Your order #{orderNumber} has been placed successfully. Total: #{total}', 'Sent when a new order is created'),
    ('ORDER_SHIPPED', 'ORDER', 'EMAIL', 'Your Order Has Shipped - #{orderNumber}', 'Good news! Your order #{orderNumber} has been shipped. Track your delivery: #{trackingUrl}', 'Sent when order is shipped'),
    ('ORDER_DELIVERED', 'ORDER', 'EMAIL', 'Order Delivered - #{orderNumber}', 'Your order #{orderNumber} has been delivered. Thank you for shopping with AgriLink!', 'Sent when order is delivered'),
    ('NEW_MESSAGE', 'MESSAGE', 'PUSH', 'New Message from #{senderName}', '#{messagePreview}', 'Sent when user receives a new message'),
    ('LISTING_SOLD', 'LISTING', 'EMAIL', 'Your Listing Has Sold!', 'Congratulations! Your listing "#{listingTitle}" has been sold for #{price}', 'Sent when a listing is purchased'),
    ('IOT_ALERT', 'IOT', 'PUSH', 'IoT Alert: #{deviceName}', '#{alertMessage}', 'Sent for IoT device alerts'),
    ('WEATHER_ALERT', 'WEATHER', 'PUSH', 'Weather Alert for #{farmName}', '#{weatherAlert}', 'Sent for weather alerts'),
    ('WELCOME', 'SYSTEM', 'EMAIL', 'Welcome to AgriLink!', 'Welcome to AgriLink, #{userName}! Start exploring our marketplace.', 'Welcome email for new users');
