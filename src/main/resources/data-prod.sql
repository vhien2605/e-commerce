
INSERT INTO permissions (id, create_at, update_at, name)
SELECT 1, NULL, NULL, 'read_user'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 1);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 2, NULL, NULL, 'create_user'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 2);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 3, NULL, NULL, 'update_user'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 3);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 4, NULL, NULL, 'delete_user'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 4);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 5, NULL, NULL, 'read_role'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 5);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 6, NULL, NULL, 'create_role'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 6);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 7, NULL, NULL, 'update_role'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 7);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 8, NULL, NULL, 'delete_role'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 8);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 9, NULL, NULL, 'read_product'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 9);

INSERT INTO permissions (id, create_at, update_at, name)
SELECT 10, NULL, NULL, 'create_product'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE id = 10);




INSERT INTO roles (id, create_at, update_at, name)
SELECT 1, NULL, NULL, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 1);

INSERT INTO roles (id, create_at, update_at, name)
SELECT 2, NULL, NULL, 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 2);

INSERT INTO roles (id, create_at, update_at, name)
SELECT 3, NULL, NULL, 'SELLER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 3);

INSERT INTO roles (id, create_at, update_at, name)
SELECT 4, NULL, NULL, 'SYSTEM_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 4);



-- ADMIN (id=1) gets user, role, product, and shop management
INSERT INTO roles_permissions (role_id, permission_id)
SELECT 1, p.id FROM permissions p
WHERE p.name IN ('read_user','create_user','update_user','delete_user',
                 'read_role','create_role','update_role','delete_role',
                 'read_product','create_product','update_product','delete_product',
                 'read_shop','create_shop','update_shop','delete_shop')
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = 1 AND rp.permission_id = p.id
);

-- CUSTOMER (id=2) gets order-related permissions
INSERT INTO roles_permissions (role_id, permission_id)
SELECT 2, p.id FROM permissions p
WHERE p.name IN ('read_order','create_order','update_order','delete_order')
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = 2 AND rp.permission_id = p.id
);

-- SELLER (id=3) gets product + shop management
INSERT INTO roles_permissions (role_id, permission_id)
SELECT 3, p.id FROM permissions p
WHERE p.name IN ('read_product','create_product','update_product','delete_product',
                 'read_shop','create_shop','update_shop','delete_shop')
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = 3 AND rp.permission_id = p.id
);

-- SYSTEM_ADMIN (id=4) gets ALL permissions
INSERT INTO roles_permissions (role_id, permission_id)
SELECT 4, p.id FROM permissions p
WHERE NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = 4 AND rp.permission_id = p.id
);


INSERT INTO addresses (id, create_at, update_at, city, country, name)
SELECT 1, NULL, NULL, 'Nam Định', 'Việt Nam', 'Trung Đông'
WHERE NOT EXISTS (SELECT 1 FROM addresses WHERE id = 1);

INSERT IGNORE INTO users (id, create_at, update_at, email, gender, password, status, username, address_id, full_name)
VALUES (1, NULL, NULL, 'hvu6582@gmail.com', 'MALE', '$2a$10$AeH4T4qmk.rc/qUq4JwlU.dz7X5sQB05IfpYY8HAFP32yUvmOa5da', 'ACTIVE', 'hvu6582', 1, 'Trần Đinh');

-- User 1 → SYSTEM_ADMIN
INSERT INTO users_roles (user_id, role_id)
SELECT 1, 4
WHERE NOT EXISTS (
    SELECT 1 FROM users_roles WHERE user_id = 1 AND role_id = 4
);

