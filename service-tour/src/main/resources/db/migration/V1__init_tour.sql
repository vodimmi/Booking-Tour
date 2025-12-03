-- ================================
-- Create tour_categories table
-- ================================
CREATE TABLE tour_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Insert initial tour categories
INSERT INTO tour_categories (name, description, status) VALUES
                                                            ('Du lịch biển', 'Các tour nghỉ dưỡng biển', 'ACTIVE'),
                                                            ('Leo núi', 'Tour leo núi', 'ACTIVE');



-- ================================
-- Create locations table
-- ================================
CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    province VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Insert initial locations
INSERT INTO locations (name, country, province, description, status) VALUES
                                                                         ('Phú Quốc', 'Việt Nam', 'Kiên Giang', 'Đảo ngọc', 'ACTIVE'),
                                                                         ('Sa Pa', 'Việt Nam', 'Lào Cai', 'Thị trấn mây', 'ACTIVE');



-- ================================
-- Create tours table
-- ================================
CREATE TABLE tours (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       price DOUBLE,
                       image VARCHAR(255),
                       rating DOUBLE DEFAULT 0.0,
                       review_count INT DEFAULT 0,
                       start_date DATE,
                       end_date DATE,
                       status VARCHAR(20) DEFAULT 'DRAFT',
                       category_id BIGINT NOT NULL,
                       location_id BIGINT NOT NULL,
                       duration_days INT NOT NULL,
                       max_people INT NOT NULL,
                       available_slots INT NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (category_id) REFERENCES tour_categories(id),
                       FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- Insert initial tours
INSERT INTO tours (
    name, description, category_id, location_id,
    duration_days, max_people, available_slots, price, status
) VALUES
      ('Tour Phú Quốc 3N2Đ', 'Nghỉ dưỡng và lặn biển', 1, 1, 3, 30, 25, 2500000, 'ACTIVE'),
      ('Tour Sa Pa 2N1Đ', 'Leo núi và săn mây', 2, 2, 2, 20, 12, 1500000, 'ACTIVE');
