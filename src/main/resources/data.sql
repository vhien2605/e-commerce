INSERT INTO roles (name)
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'CUSTOMER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CUSTOMER');

INSERT INTO roles (name)
SELECT 'SELLER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SELLER');


INSERT INTO permissions (name)
SELECT 'read_user' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'read_user');

INSERT INTO permissions (name)
SELECT 'create_user' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'create_user');

INSERT INTO permissions (name)
SELECT 'update_user' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'update_user');

INSERT INTO permissions (name)
SELECT 'delete_user' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'delete_user');

INSERT INTO permissions (name)
SELECT 'read_product' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'read_product');

INSERT INTO permissions (name)
SELECT 'create_product' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'create_product');

INSERT INTO permissions (name)
SELECT 'update_product' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'update_product');

INSERT INTO permissions (name)
SELECT 'delete_product' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'delete_product');

INSERT INTO permissions (name)
SELECT 'create_cart' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'create_cart');


INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id
);


INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'CUSTOMER' AND p.name = 'read_product'
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id
);


INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'SELLER' AND p.name IN ('read_product', 'create_product', 'update_product', 'delete_product')
AND NOT EXISTS (
    SELECT 1 FROM roles_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id
);

