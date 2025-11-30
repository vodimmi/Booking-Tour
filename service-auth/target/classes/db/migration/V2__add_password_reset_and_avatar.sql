-- Phase 2: Add password reset tokens and avatar_url field

-- Add avatar_url column to users table
ALTER TABLE users ADD COLUMN avatar_url VARCHAR(500) AFTER full_name;

-- Create password_reset_tokens table
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add index to refresh_tokens.token for better performance
CREATE INDEX idx_refresh_token ON refresh_tokens(token);

-- Add index to users.email for better performance  
CREATE INDEX idx_user_email ON users(email);
