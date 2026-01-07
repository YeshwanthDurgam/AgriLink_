-- Create databases for each service
CREATE DATABASE agrilink_auth;
CREATE DATABASE agrilink_user;
CREATE DATABASE agrilink_farm;
CREATE DATABASE agrilink_marketplace;
CREATE DATABASE agrilink_order;
CREATE DATABASE agrilink_iot;
CREATE DATABASE agrilink_notification;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE agrilink_auth TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_user TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_farm TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_marketplace TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_order TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_iot TO agrilink;
GRANT ALL PRIVILEGES ON DATABASE agrilink_notification TO agrilink;
