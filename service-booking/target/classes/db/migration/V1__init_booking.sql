CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tour_id BIGINT NOT NULL,
    number_of_people INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    booking_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    rejection_reason VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    
    -- Constraints
    CONSTRAINT chk_number_of_people CHECK (number_of_people > 0),
    CONSTRAINT chk_total_price CHECK (total_price >= 0),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED'))
);

-- Indexes for better performance
CREATE INDEX idx_user_bookings ON bookings(user_id, status);
CREATE INDEX idx_tour_bookings ON bookings(tour_id, status);
CREATE INDEX idx_booking_date ON bookings(booking_date);

-- Example status trigger to set updated_at
DELIMITER //
CREATE TRIGGER trg_booking_update 
    BEFORE UPDATE ON bookings
    FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END//
DELIMITER ;