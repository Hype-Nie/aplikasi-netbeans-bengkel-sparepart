CREATE DATABASE IF NOT EXISTS bengkel_sparepart
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bengkel_sparepart;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS sale_transaction_items;
DROP TABLE IF EXISTS sale_transactions;
DROP TABLE IF EXISTS purchase_transaction_items;
DROP TABLE IF EXISTS purchase_transactions;
DROP TABLE IF EXISTS service_transaction_items;
DROP TABLE IF EXISTS service_transactions;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS spareparts;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS admins;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(30),
    vehicle VARCHAR(100),
    address VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(120),
    address VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE spareparts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    part_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(80),
    unit VARCHAR(20),
    purchase_price DECIMAL(14,2) NOT NULL DEFAULT 0,
    selling_price DECIMAL(14,2) NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    min_stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Master table for service types (jasa)
CREATE TABLE services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    price DECIMAL(14,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE service_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_no VARCHAR(30) NOT NULL UNIQUE,
    service_date DATE NOT NULL,
    customer_id INT NOT NULL,
    vehicle VARCHAR(100),
    complaint TEXT,
    mechanic VARCHAR(100),
    status VARCHAR(30) NOT NULL,
    total DECIMAL(14,2) NOT NULL DEFAULT 0,
    created_by INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_service_admin FOREIGN KEY (created_by) REFERENCES admins(id)
) ENGINE=InnoDB;

CREATE TABLE service_transaction_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_transaction_id INT NOT NULL,
    item_type VARCHAR(20) NOT NULL,
    sparepart_id INT NULL,
    service_id INT NULL,
    description VARCHAR(200) NOT NULL,
    qty INT NOT NULL,
    price DECIMAL(14,2) NOT NULL,
    subtotal DECIMAL(14,2) NOT NULL,
    CONSTRAINT fk_service_items_header FOREIGN KEY (service_transaction_id) REFERENCES service_transactions(id)
      ON DELETE CASCADE,
    CONSTRAINT fk_service_items_sparepart FOREIGN KEY (sparepart_id) REFERENCES spareparts(id),
    CONSTRAINT fk_service_items_service FOREIGN KEY (service_id) REFERENCES services(id)
) ENGINE=InnoDB;

CREATE TABLE purchase_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    purchase_no VARCHAR(30) NOT NULL UNIQUE,
    purchase_date DATE NOT NULL,
    supplier_id INT NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(30) NOT NULL,
    total DECIMAL(14,2) NOT NULL DEFAULT 0,
    created_by INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_purchase_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_purchase_admin FOREIGN KEY (created_by) REFERENCES admins(id)
) ENGINE=InnoDB;

CREATE TABLE purchase_transaction_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    purchase_transaction_id INT NOT NULL,
    sparepart_id INT NOT NULL,
    part_code VARCHAR(20) NOT NULL,
    part_name VARCHAR(120) NOT NULL,
    qty INT NOT NULL,
    unit_price DECIMAL(14,2) NOT NULL,
    subtotal DECIMAL(14,2) NOT NULL,
    CONSTRAINT fk_purchase_items_header FOREIGN KEY (purchase_transaction_id) REFERENCES purchase_transactions(id)
      ON DELETE CASCADE,
    CONSTRAINT fk_purchase_items_sparepart FOREIGN KEY (sparepart_id) REFERENCES spareparts(id)
) ENGINE=InnoDB;

-- Direct spare part counter sales
CREATE TABLE sale_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_no VARCHAR(30) NOT NULL UNIQUE,
    sale_date DATE NOT NULL,
    customer_id INT NULL,
    payment_method VARCHAR(50),
    total DECIMAL(14,2) NOT NULL DEFAULT 0,
    created_by INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sale_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_sale_admin FOREIGN KEY (created_by) REFERENCES admins(id)
) ENGINE=InnoDB;

CREATE TABLE sale_transaction_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_transaction_id INT NOT NULL,
    sparepart_id INT NOT NULL,
    part_code VARCHAR(20) NOT NULL,
    part_name VARCHAR(120) NOT NULL,
    qty INT NOT NULL,
    unit_price DECIMAL(14,2) NOT NULL,
    subtotal DECIMAL(14,2) NOT NULL,
    CONSTRAINT fk_sale_items_header FOREIGN KEY (sale_transaction_id) REFERENCES sale_transactions(id)
      ON DELETE CASCADE,
    CONSTRAINT fk_sale_items_sparepart FOREIGN KEY (sale_transaction_id) REFERENCES sale_transactions(id)
) ENGINE=InnoDB;

-- ============================================================
-- SEED DATA
-- ============================================================

INSERT INTO admins (username, password, full_name, role, is_active) VALUES
('admin', 'admin123', 'Administrator Bengkel', 'ADMIN', 1);

INSERT INTO customers (customer_code, name, phone, vehicle, address) VALUES
('PLG-001', 'Budi Santoso', '08121234567', 'Honda Beat', 'Jl. Mawar 10'),
('PLG-002', 'Anisa Putri', '08129876543', 'Yamaha NMAX', 'Jl. Melati 2'),
('PLG-003', 'Dedi Mulyana', '08211234567', 'Suzuki Ertiga', 'Jl. Kenanga 8');

INSERT INTO suppliers (supplier_code, name, phone, email, address) VALUES
('SUP-001', 'CV Sumber Jaya', '021889977', 'sumber@jaya.id', 'Jl. Industri 1'),
('SUP-002', 'PT Part Nusantara', '021778899', 'part@nusantara.id', 'Jl. Raya Barat 32'),
('SUP-003', 'UD Prima Motor', '021665544', 'prima@motor.id', 'Jl. Taman 4');

INSERT INTO spareparts (part_code, name, category, unit, purchase_price, selling_price, stock, min_stock) VALUES
('SP-0001', 'Oli Mesin 10W-40', 'Pelumas', 'Botol', 75000, 95000, 24, 5),
('SP-0002', 'Kampas Rem Depan', 'Pengereman', 'Set', 62000, 85000, 16, 5),
('SP-0003', 'Busi Iridium', 'Kelistrikan', 'Pcs', 38000, 55000, 40, 8),
('SP-0004', 'Filter Udara', 'Mesin', 'Pcs', 24000, 35000, 4, 5);

INSERT INTO services (service_code, name, price) VALUES
('JS-001', 'Jasa Tune Up', 150000),
('JS-002', 'Jasa Ganti Oli', 30000),
('JS-003', 'Jasa Ganti Kampas Rem', 50000),
('JS-004', 'Jasa Servis Ringan', 75000),
('JS-005', 'Jasa Servis Besar', 250000);

INSERT INTO service_transactions (service_no, service_date, customer_id, vehicle, complaint, mechanic, status, total, created_by)
VALUES
('SRV-00001', CURRENT_DATE, 1, 'Honda Beat', 'Mesin kasar dan perlu ganti oli', 'Riyan', 'SELESAI', 245000, 1),
('SRV-00002', CURRENT_DATE, 2, 'Yamaha NMAX', 'Rem depan kurang pakem', 'Asep', 'PROSES', 135000, 1);

INSERT INTO service_transaction_items (service_transaction_id, item_type, sparepart_id, service_id, description, qty, price, subtotal)
VALUES
(1, 'JASA', NULL, 1, 'Jasa Tune Up', 1, 150000, 150000),
(1, 'PART', 1, NULL, 'Oli Mesin 10W-40', 1, 95000, 95000),
(2, 'JASA', NULL, 3, 'Jasa Ganti Kampas Rem', 1, 50000, 50000),
(2, 'PART', 2, NULL, 'Kampas Rem Depan', 1, 85000, 85000);

INSERT INTO purchase_transactions (purchase_no, purchase_date, supplier_id, payment_method, status, total, created_by)
VALUES
('PO-00001', CURRENT_DATE, 1, 'Transfer', 'SELESAI', 2430000, 1);

INSERT INTO purchase_transaction_items (purchase_transaction_id, sparepart_id, part_code, part_name, qty, unit_price, subtotal)
VALUES
(1, 1, 'SP-0001', 'Oli Mesin 10W-40', 20, 75000, 1500000),
(1, 2, 'SP-0002', 'Kampas Rem Depan', 15, 62000, 930000);

INSERT INTO sale_transactions (sale_no, sale_date, customer_id, payment_method, total, created_by)
VALUES
('SAL-00001', CURRENT_DATE, NULL, 'Tunai', 150000, 1);

INSERT INTO sale_transaction_items (sale_transaction_id, sparepart_id, part_code, part_name, qty, unit_price, subtotal)
VALUES
(1, 3, 'SP-0003', 'Busi Iridium', 2, 55000, 110000),
(1, 4, 'SP-0004', 'Filter Udara', 1, 35000, 35000);
