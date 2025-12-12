-- Database initialization for RealEstateHub
-- Adjust encoding/engine as needed before running in your environment.

CREATE DATABASE IF NOT EXISTS realestatehub
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;
USE realestatehub;

-- Users
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50),
    role VARCHAR(20) NOT NULL,
    enabled BIT NOT NULL DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB;

-- Agents
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    agency VARCHAR(100),
    license_no VARCHAR(100),
    bio VARCHAR(500),
    wechat VARCHAR(100),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_agent_user FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB;

-- Admins
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    super_admin BIT NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB;

-- Houses
CREATE TABLE IF NOT EXISTS house (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(2000),
    price DECIMAL(15,2),
    area DOUBLE,
    region VARCHAR(50),
    address VARCHAR(100),
    layout VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    approved BIT NOT NULL DEFAULT 0,
    publish_time DATETIME,
    view_count BIGINT DEFAULT 0,
    agent_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_house_agent FOREIGN KEY (agent_id) REFERENCES agent (id)
) ENGINE=InnoDB;

-- House images
CREATE TABLE IF NOT EXISTS house_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    sort_order INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_house_image_house FOREIGN KEY (house_id) REFERENCES house (id)
) ENGINE=InnoDB;

-- House tags
CREATE TABLE IF NOT EXISTS house_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_house_tag_house FOREIGN KEY (house_id) REFERENCES house (id)
) ENGINE=InnoDB;

-- Favorites
CREATE TABLE IF NOT EXISTS favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    house_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY uk_fav_user_house (user_id, house_id),
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_fav_house FOREIGN KEY (house_id) REFERENCES house (id)
) ENGINE=InnoDB;

-- Appointments
CREATE TABLE IF NOT EXISTS appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    house_id BIGINT NOT NULL,
    agent_id BIGINT,
    scheduled_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    remark VARCHAR(500),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_appointment_house FOREIGN KEY (house_id) REFERENCES house (id),
    CONSTRAINT fk_appointment_agent FOREIGN KEY (agent_id) REFERENCES agent (id)
) ENGINE=InnoDB;

-- Browse logs (optional)
CREATE TABLE IF NOT EXISTS browse_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    house_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_browse_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_browse_house FOREIGN KEY (house_id) REFERENCES house (id)
) ENGINE=InnoDB;

