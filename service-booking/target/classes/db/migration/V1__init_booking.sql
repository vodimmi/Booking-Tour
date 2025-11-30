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

USE BT_BOOKING; 

INSERT INTO bookings (user_id, tour_id, number_of_people, total_price, booking_date, status, rejection_reason, created_at) 
VALUES (2,100,2,150.00,'2025-12-05 09:00:00','PENDING',NULL,NOW()), (11,101,4,600.00,'2025-12-10 14:00:00','CONFIRMED',NULL,NOW()), 
       (2,102,1,50.00,'2025-12-01 08:00:00','REJECTED','Overbooked',NOW()), 
       (2,103,3,300.00,'2025-12-15 10:00:00','CANCELLED',NULL,NOW()), (14,104,5,1250.00,'2026-01-05 12:00:00','PENDING',NULL,NOW()); 
SELECT id, user_id, tour_id, number_of_people, total_price, booking_date, status, rejection_reason, created_at 
FROM bookings 
ORDER BY id 
DESC LIMIT 10;