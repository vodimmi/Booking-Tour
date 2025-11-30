CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tour_id BIGINT NOT NULL,
    number_of_people INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    booking_date DATETIME NOT NULL,
    tour_start_date DATE,
    tour_end_date DATE,
    status VARCHAR(20) NOT NULL,
    rejection_reason VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,

    CONSTRAINT chk_number_of_people CHECK (number_of_people > 0),
    CONSTRAINT chk_total_price CHECK (total_price >= 0),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED'))
);

CREATE INDEX idx_user_bookings ON bookings(user_id, status);
CREATE INDEX idx_tour_bookings ON bookings(tour_id, status);
CREATE INDEX idx_booking_date ON bookings(booking_date);
CREATE INDEX idx_tour_start_date ON bookings(tour_start_date);

CREATE TRIGGER trg_booking_update
BEFORE UPDATE ON bookings
FOR EACH ROW
SET NEW.updated_at = CURRENT_TIMESTAMP;
