-- V2__seed_bookings.sql
-- Seed data for bookings. This migration avoids DELIMITER/SELECT/USE statements so it works with Flyway/JDBC.
-- Insert a handful of example bookings that satisfy the table constraints.

INSERT INTO bookings (user_id, tour_id, number_of_people, total_price, booking_date, status, rejection_reason, created_at)
VALUES
  (2, 200, 2, 150.00, '2025-12-05 09:00:00', 'PENDING', NULL, NOW()),
  (11, 201, 4, 600.00, '2025-12-10 14:00:00', 'CONFIRMED', NULL, NOW()),
  (3, 202, 1, 50.00, '2025-12-01 08:00:00', 'REJECTED', 'Overbooked', NOW()),
  (4, 203, 3, 300.00, '2025-12-15 10:00:00', 'CANCELLED', NULL, NOW()),
  (14, 204, 5, 1250.00, '2026-01-05 12:00:00', 'PENDING', NULL, NOW()),
  (7, 205, 2, 220.00, '2025-12-20 16:30:00', 'CONFIRMED', NULL, NOW());

-- Optional: if you want to make this idempotent across re-runs, you could wrap inserts in
-- conditional logic or use a separate check. Flyway runs migrations once, so simple INSERTs are fine.
