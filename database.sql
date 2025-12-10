-- Database initialization for RealEstateHub
-- Adjust encoding/engine as needed before running in your environment.
CREATE DATABASE IF NOT EXISTS realestatehub
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
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


USE realestatehub;

-- 插入默认用户
INSERT INTO user (phone, password, name, role, enabled, created_at, updated_at)
VALUES ('18800000000', '123456', 'admin', 'ADMIN', 1, NOW(), NOW());

-- 获取刚插入用户的 id
SET @user_id = LAST_INSERT_ID();

-- 插入管理员信息
INSERT INTO admin (user_id, super_admin, created_at, updated_at)
VALUES (@user_id, 1, NOW(), NOW());

-- 插入房源
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO house (title, description, price, area, region, address, layout, status, approved, publish_time, view_count, agent_id, created_at, updated_at)
VALUES
    ('朝阳公寓', '位于北京市朝阳区，交通便利，采光好。', 3500000.00, 90.0, '北京市-朝阳区', '朝阳北路123号', '2室1厅', 'AVAILABLE', 1, NOW(), 0, 1, NOW(), NOW()),
    ('浦东豪宅', '上海浦东新区高档住宅，带花园和地下车库。', 12000000.00, 250.0, '上海市-浦东新区', '陆家嘴东路456号', '4室3厅', 'AVAILABLE', 1, NOW(), 0, 1, NOW(), NOW()),
    ('深圳福田小区', '深圳福田区现代小区，周边配套完善。', 5000000.00, 110.0, '深圳市-福田区', '福田街道789号', '3室2厅', 'AVAILABLE', 1, NOW(), 0, 1, NOW(), NOW()),
    ('广州天河公寓', '广州天河区精装修公寓，交通便利。', 3200000.00, 85.0, '广州市-天河区', '天河路321号', '2室1厅', 'AVAILABLE', 1, NOW(), 0, 1, NOW(), NOW()),
    ('杭州西湖别墅', '杭州西湖区别墅，环境优美，景观好。', 8000000.00, 200.0, '杭州市-西湖区', '西湖路654号', '4室3厅', 'AVAILABLE', 1, NOW(), 0, 1, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;
