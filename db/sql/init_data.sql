INSERT INTO roles (role_id, name) VALUES (1, 'user') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_id, name) VALUES (2, 'admin') ON CONFLICT DO NOTHING;

INSERT INTO users (user_id, login, password, email, first_name, last_name, birthday, role_id) VALUES (1, 'admin', '$2a$10$Z9HXp/W6cFBFteM2fG9HIe7WzfEHpAk6GULdY7Cckqm5.PjeiP7Gm', 'admin@email.com', 'Admin', 'Johnson', '1990-01-21', 2) ON CONFLICT DO NOTHING;
INSERT INTO users (user_id, login, password, email, first_name, last_name, birthday, role_id) VALUES (2, 'mromney', '$2a$10$C2W6FdzduGrtF6vAQ4LRXef4evUfdJqb8kPafjTmY9u1naN5YGAJq', 'mromney@email.com', 'Michael', 'Romney', '1997-02-07', 1) ON CONFLICT DO NOTHING;

SELECT setval('roles_role_id_seq', max(role_id)) FROM roles;
SELECT setval('users_user_id_seq', max(user_id)) FROM users;
