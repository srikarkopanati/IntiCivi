INSERT INTO users (id, name, email, password, role) VALUES
(1, 'Administrator', 'admin@inticivi.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOijBgHLO6wxgq/9bYQ0eF4Kc0R4qqZqO', 'ADMIN'),
(2, 'Demo User', 'user@inticivi.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOijBgHLO6wxgq/9bYQ0eF4Kc0R4qqZqO', 'USER');

INSERT INTO complaints (id, title, description, category, location, pincode, latitude, longitude, image_url, priority_score, status, created_at, updated_at, created_by) VALUES
(1, 'Broken streetlight in downtown', 'The streetlight near the central market is broken and has been dark for several nights.', 'high', 'Downtown Central Market', '560001', 12.9716, 77.5946, 'https://example.com/images/streetlight.jpg', 8.5, 'PENDING', NOW(), NOW(), 'user@inticivi.com'),
(2, 'Illegal dumping near river', 'There is illegal waste dumping near the riverbank, causing health and environment hazards.', 'critical', 'Riverbend Area', '560002', 12.9622, 77.5750, 'https://example.com/images/dumping.jpg', 9.6, 'IN_PROGRESS', NOW(), NOW(), 'user@inticivi.com');
