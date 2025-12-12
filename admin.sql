INSERT INTO user (phone, password, name, role, enabled, created_at, updated_at)
VALUES (
           'admin',
           '$2a$10$u4T0bq2hnMZyKZfS0XyN6e9pLsaHnZcgB/xZpYjA8fD7L3Bik0y9G', -- 123456 的 BCrypt
           '管理员',
           'ADMIN',
           1,
           NOW(),
           NOW()
       );


# UPDATE user
# SET password = '$2a$10$7qj4sVqZV1Fh5MKMkB0Gge9qxFhS9pQq9J6jz7gqA4L/hR7N3SdIq'
# WHERE phone = '13800000000';


USE realestatehub;

-- 1) 创建经纪人账号（密码为 123456，对应的 bcrypt）
INSERT INTO user (phone, password, name, role, enabled, created_at, updated_at)
VALUES
    ('13800000001', '$2a$10$u1qMfCVxYv3BPjVAk0nn1uQ9VHtV6nRvWmvMRGsiE9zraFMvx6bBa', '张经纪', 'AGENT', 1, NOW(), NOW()),
    ('13800000002', '$2a$10$u1qMfCVxYv3BPjVAk0nn1uQ9VHtV6nRvWmvMRGsiE9zraFMvx6bBa', '李经纪', 'AGENT', 1, NOW(), NOW());

-- 2) 生成经纪人信息
SET @agent1 := (SELECT id FROM user WHERE phone='13800000001');
SET @agent2 := (SELECT id FROM user WHERE phone='13800000002');

INSERT INTO agent (user_id, agency, license_no, bio, wechat, created_at, updated_at)
VALUES
    (@agent1, '阳光地产', 'LIC-0001', '十年新房/二手房经验', 'agent1wx', NOW(), NOW()),
    (@agent2, '城市置业', 'LIC-0002', '熟悉学区与地铁盘', 'agent2wx', NOW(), NOW());

-- 3) 房源主表
INSERT INTO house (title, description, price, area, region, address, layout, status, approved, publish_time, view_count, agent_id, created_at, updated_at)
VALUES
    ('滨河花园 · 89㎡ 南北通透', '临河景观，两房改三房，满五唯一', 3650000, 89.0, '滨江区', '滨河路 88 号', '3室2厅1卫', 'PUBLISHED', 1, NOW(), 128, (SELECT id FROM agent WHERE user_id=@agent1), NOW(), NOW()),
    ('中心公寓 · 65㎡ 小三房', '近地铁 200 米，带精装拎包入住', 2980000, 65.0, '江汉区', '建设大道 188 号', '3室1厅1卫', 'PUBLISHED', 1, NOW(), 86, (SELECT id FROM agent WHERE user_id=@agent1), NOW(), NOW()),
    ('星海湾 · 120㎡ 复式', '顶复带露台，采光通风优秀', 5280000, 120.0, '高新园区', '星海路 66 号', '4室2厅2卫', 'PUBLISHED', 1, NOW(), 203, (SELECT id FROM agent WHERE user_id=@agent2), NOW(), NOW()),
    ('学府名邸 · 98㎡ 三房', '双学区，南北通透中高楼层', 4150000, 98.0, '南山区', '科苑南路 28 号', '3室2厅2卫', 'PUBLISHED', 1, NOW(), 155, (SELECT id FROM agent WHERE user_id=@agent2), NOW(), NOW()),
    ('绿荫里 · 76㎡ 两房', '性价比刚需盘，近公园与商圈', 2680000, 76.0, '余杭区', '文一西路 399 号', '2室2厅1卫', 'PUBLISHED', 1, NOW(), 74, (SELECT id FROM agent WHERE user_id=@agent1), NOW(), NOW());

-- 4) 房源图片
INSERT INTO house_image (house_id, url, sort_order, created_at, updated_at)
VALUES
    ((SELECT id FROM house WHERE title LIKE '滨河花园%'), 'https://picsum.photos/seed/house1a/800/600', 1, NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '滨河花园%'), 'https://picsum.photos/seed/house1b/800/600', 2, NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '中心公寓%'), 'https://picsum.photos/seed/house2a/800/600', 1, NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '星海湾%'), 'https://picsum.photos/seed/house3a/800/600', 1, NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '学府名邸%'), 'https://picsum.photos/seed/house4a/800/600', 1, NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '绿荫里%'), 'https://picsum.photos/seed/house5a/800/600', 1, NOW(), NOW());

-- 5) 房源标签
INSERT INTO house_tag (house_id, tag, created_at, updated_at)
VALUES
    ((SELECT id FROM house WHERE title LIKE '滨河花园%'), '临河景观', NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '滨河花园%'), '满五唯一', NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '中心公寓%'), '近地铁', NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '星海湾%'), '复式露台', NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '学府名邸%'), '双学区', NOW(), NOW()),
    ((SELECT id FROM house WHERE title LIKE '绿荫里%'), '公园旁', NOW(), NOW());
