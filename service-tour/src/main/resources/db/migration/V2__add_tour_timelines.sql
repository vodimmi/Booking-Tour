-- Create tour_timelines table
CREATE TABLE tour_timelines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT NOT NULL,
    day_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE,
    INDEX idx_tour_timeline (tour_id, day_number)
);

-- Insert sample timelines for Tour Phú Quốc 3N2Đ (tour_id = 1)
INSERT INTO tour_timelines (tour_id, day_number, title, description, start_time, end_time) VALUES
(1, 1, 'Đón khách tại sân bay', 'Xe đón khách tại sân bay Phú Quốc, di chuyển về khách sạn check-in', '08:00:00', '10:00:00'),
(1, 1, 'Tham quan chợ đêm', 'Khám phá ẩm thực địa phương tại chợ đêm Phú Quốc', '18:00:00', '21:00:00'),
(1, 2, 'Tour 4 đảo', 'Lặn ngắm san hô, câu cá, thưởng thức hải sản tươi sống', '08:00:00', '16:00:00'),
(1, 2, 'Nghỉ ngơi tự do', 'Thư giãn tại resort hoặc khám phá quanh khu vực', '16:00:00', '22:00:00'),
(1, 3, 'Tham quan Vinpearl Safari', 'Khám phá vườn thú bán hoang dã lớn nhất Việt Nam', '08:00:00', '12:00:00'),
(1, 3, 'Trả phòng và đưa tiễn', 'Check-out khách sạn, di chuyển ra sân bay', '14:00:00', '16:00:00');

-- Insert sample timelines for Tour Sa Pa 2N1Đ (tour_id = 2)
INSERT INTO tour_timelines (tour_id, day_number, title, description, start_time, end_time) VALUES
(2, 1, 'Khởi hành từ Hà Nội', 'Xe đưa đón tại điểm hẹn, di chuyển lên Sa Pa', '06:00:00', '11:00:00'),
(2, 1, 'Trekking bản Cát Cát', 'Leo núi nhẹ, tham quan bản làng người H\'Mông', '13:00:00', '17:00:00'),
(2, 1, 'Thưởng thức BBQ địa phương', 'Buffet nướng và lẩu tại nhà hàng view núi', '18:00:00', '20:00:00'),
(2, 2, 'Chinh phục đỉnh Fansipan', 'Cáp treo lên đỉnh núi cao nhất Đông Dương', '07:00:00', '12:00:00'),
(2, 2, 'Trở về Hà Nội', 'Di chuyển về Hà Nội, kết thúc chuyến đi', '13:00:00', '18:00:00');
