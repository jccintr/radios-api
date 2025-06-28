INSERT INTO categories(name) VALUES ('Esportes');
INSERT INTO categories(name) VALUES ('Notícias');
INSERT INTO categories(name) VALUES ('Rock');
INSERT INTO categories(name) VALUES ('Pop');
INSERT INTO categories(name) VALUES ('Sertanejo');
INSERT INTO categories(name) VALUES ('Gospel');
INSERT INTO categories(name) VALUES ('Católica');
INSERT INTO categories(name) VALUES ('Carnaval');
INSERT INTO categories(name) VALUES ('Samba e Pagode');
INSERT INTO cities(name,state) VALUES ('São Paulo','SP');
INSERT INTO cities(name,state) VALUES ('Campos do Jordão','SP');
INSERT INTO cities(name,state) VALUES ('Rio de Janeiro','RJ');

INSERT INTO users (name, email, password) VALUES ('Alex', 'alex@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Maria',  'maria@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Julio Cesar',  'jccintr@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');


INSERT INTO roles (authority) VALUES ('ROLE_ADMIN');
INSERT INTO roles (authority) VALUES ('ROLE_COMMON');

INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 1);