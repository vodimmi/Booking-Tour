-- Seed initial users and assign roles (idempotent)

-- insert users (skip if email exists)
INSERT INTO users (email, password_hash, full_name, is_active)
SELECT 'admin@example.com', '$2b$10$xTk7LGCL652PCs2bsfg4uexq2waDyWzvNp189j1CgPAb2WCji3K/2', 'System Administrator', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@example.com');

INSERT INTO users (email, password_hash, full_name, is_active)
SELECT 'user@example.com', '$2b$10$oRt/9Voc86JemefoxclwmuMcyuecMkJZ3aBbYSQadCLfY/A1XhMuy', 'Regular User', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='user@example.com');

-- assign roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name='ADMIN' WHERE u.email='admin@example.com'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name='USER' WHERE u.email='user@example.com'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);
