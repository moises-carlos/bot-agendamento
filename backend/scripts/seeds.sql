INSERT INTO roles(name) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES ('STAFF') ON CONFLICT DO NOTHING;

INSERT INTO users(name, email, password)
VALUES ('Administrador', 'admin@bot.local', '$2a$10$YEn4fQ4zn8SKZX4jQuPe5.KhQ32pI.BGX3YeliYg5OtTSGTN/xG2S')
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE u.email = 'admin@bot.local'
ON CONFLICT DO NOTHING;
