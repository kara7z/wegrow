-- CREATE DATABASE IF NOT EXISTS wegrow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE wegrow;

CREATE TABLE IF NOT EXISTS departments (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL,
  description TEXT         NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_departments_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS employees (
  id                  BIGINT       NOT NULL AUTO_INCREMENT,
  registration_number VARCHAR(20)  NOT NULL,
  first_name          VARCHAR(100) NOT NULL,
  last_name           VARCHAR(100) NOT NULL,
  position            VARCHAR(100) NULL,
  contract_type       ENUM('PERMANENT','FIXED_TERM','INTERNSHIP') NOT NULL DEFAULT 'PERMANENT',
  department_id       BIGINT       NULL, 
  PRIMARY KEY (id),
  UNIQUE KEY uq_employees_registration (registration_number),
  KEY idx_employees_department (department_id),
  CONSTRAINT fk_employees_department
    FOREIGN KEY (department_id) REFERENCES departments (id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
