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