-- Seed initial users and assign roles (idempotent)

-- Insert roles
INSERT INTO roles (name)
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

-- insert users (skip if email exists)
-- Password: Admin@123
INSERT INTO users (email, password_hash, full_name, is_active)
SELECT 'admin@example.com', '$2a$10$WyX.ZGSFUvoZ.RIv.kMxzOyETtToSUL0Sw8NuHR6o42py3xleYiGu', 'System Administrator', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@example.com');

-- Password: User@123
INSERT INTO users (email, password_hash, full_name, is_active)
SELECT 'user@example.com', '$2a$10$9gvs0mjqRNvLTY.eVUOQ2.jb0csLx/sHbY92CKrt96odypVVxCbdK', 'Regular User', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user@example.com');

-- assign roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name='ADMIN' WHERE u.email='admin@example.com'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name='USER' WHERE u.email='user@example.com'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);
