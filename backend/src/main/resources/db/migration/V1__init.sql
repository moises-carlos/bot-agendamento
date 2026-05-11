CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_id BIGINT NOT NULL REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(160) NOT NULL,
    phone VARCHAR(40) NOT NULL UNIQUE,
    email VARCHAR(160),
    recurring BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE service_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    duration_minutes INT NOT NULL,
    buffer_minutes INT NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE business_hours (
    id BIGSERIAL PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE holidays (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    date DATE NOT NULL UNIQUE,
    national BOOLEAN NOT NULL
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES clients(id),
    service_type_id BIGINT NOT NULL REFERENCES service_types(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes VARCHAR(1000),
    google_event_id VARCHAR(255)
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    channel VARCHAR(40) NOT NULL,
    target VARCHAR(120) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    sent_at TIMESTAMP NOT NULL
);

CREATE TABLE conversation_context (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(40) NOT NULL UNIQUE,
    current_intent VARCHAR(60) NOT NULL,
    current_step VARCHAR(60) NOT NULL,
    context_json VARCHAR(2000),
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor VARCHAR(120) NOT NULL,
    action VARCHAR(120) NOT NULL,
    entity_name VARCHAR(120) NOT NULL,
    entity_id VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    details VARCHAR(2000)
);

INSERT INTO roles(name) VALUES ('ADMIN'), ('STAFF');

INSERT INTO service_types(name, duration_minutes, buffer_minutes, active)
VALUES ('Consulta Padrão', 60, 15, true),
       ('Retorno', 30, 10, true);

INSERT INTO business_hours(day_of_week, start_time, end_time, active)
VALUES ('MONDAY', '08:00:00', '12:00:00', true),
       ('MONDAY', '14:00:00', '18:00:00', true),
       ('TUESDAY', '08:00:00', '12:00:00', true),
       ('TUESDAY', '14:00:00', '18:00:00', true),
       ('WEDNESDAY', '08:00:00', '12:00:00', true),
       ('WEDNESDAY', '14:00:00', '18:00:00', true),
       ('THURSDAY', '08:00:00', '12:00:00', true),
       ('THURSDAY', '14:00:00', '18:00:00', true),
       ('FRIDAY', '08:00:00', '12:00:00', true),
       ('FRIDAY', '14:00:00', '18:00:00', true);
