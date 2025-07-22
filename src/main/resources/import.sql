
INSERT INTO users (name, email, password) VALUES ('Julio Cesar',  'jccintr@gmail.com', '$2a$10$8V3FA/a8QZBu9AK2LzTzlelF3s3Py1P9IIxalmq15p/jcVcZx4lIe');
INSERT INTO users (name, email, password) VALUES ('Alex', 'alex@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Maria',  'maria@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');

INSERT INTO users (name, email, password) VALUES ('Joyce',  'joyce@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Ana',  'ana@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Daniela',  'daniela@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Daniel',  'daniel@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Lucas',  'lucas@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Matheus',  'matheus@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Graça Oliveira',  'graça@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Isis Love',  'isis@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Ana Paula',  'anapaula@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Paulo',  'paulo@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Alfred',  'alfred@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Frank Black',  'frank@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');

INSERT INTO roles (authority) VALUES ('ROLE_ADMIN');
INSERT INTO roles (authority) VALUES ('ROLE_COMMON');

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 2);

INSERT INTO user_role (user_id, role_id) VALUES (4, 2);
INSERT INTO user_role (user_id, role_id) VALUES (5, 2);
INSERT INTO user_role (user_id, role_id) VALUES (6, 2);
INSERT INTO user_role (user_id, role_id) VALUES (7, 2);
INSERT INTO user_role (user_id, role_id) VALUES (8, 2);
INSERT INTO user_role (user_id, role_id) VALUES (9, 2);
INSERT INTO user_role (user_id, role_id) VALUES (10, 2);
INSERT INTO user_role (user_id, role_id) VALUES (11, 2);
INSERT INTO user_role (user_id, role_id) VALUES (12, 2);
INSERT INTO user_role (user_id, role_id) VALUES (13, 2);
INSERT INTO user_role (user_id, role_id) VALUES (14, 2);
INSERT INTO user_role (user_id, role_id) VALUES (15, 1);



INSERT INTO categories(name) VALUES ('Sugeridas');
INSERT INTO categories(name) VALUES ('Top 10');  
INSERT INTO categories(name) VALUES ('Esportes'); 
INSERT INTO categories(name) VALUES ('Notícias');
INSERT INTO categories(name) VALUES ('Hits'); 
INSERT INTO categories(name) VALUES ('Rock');
INSERT INTO categories(name) VALUES ('Pop');
INSERT INTO categories(name) VALUES ('MPB'); 
INSERT INTO categories(name) VALUES ('Dance');
INSERT INTO categories(name) VALUES ('Rap');  
INSERT INTO categories(name) VALUES ('Samba e Pagode');
INSERT INTO categories(name) VALUES ('Sertanejo');
INSERT INTO categories(name) VALUES ('Gospel');
INSERT INTO categories(name) VALUES ('Católica');
INSERT INTO categories(name) VALUES ('Carnaval');
INSERT INTO categories(name) VALUES ('Flashback');
INSERT INTO categories(name) VALUES ('Nostalgia');
INSERT INTO categories(name) VALUES ('Anos 90');
INSERT INTO categories(name) VALUES ('Anos 80');
INSERT INTO categories(name) VALUES ('Web Radio');


INSERT INTO cities(name,state) VALUES ('São Paulo','SP');
INSERT INTO cities(name,state) VALUES ('Campos do Jordão','SP');
INSERT INTO cities(name,state) VALUES ('Rio de Janeiro','RJ');
INSERT INTO cities(name,state) VALUES ('Santa Rita do Sapucaí','MG');
INSERT INTO cities(name,state) VALUES ('Belo Horizonte','MG');
INSERT INTO cities(name,state) VALUES ('São José dos Campos','SP');
INSERT INTO cities(name,state) VALUES ('Itajubá','MG');
INSERT INTO cities(name,state) VALUES ('Pouso Alegre','MG');
INSERT INTO cities(name,state) VALUES ('Taubaté','SP');
INSERT INTO cities(name,state) VALUES ('Ubatuba','SP');
INSERT INTO cities(name,state) VALUES ('Brazópolis','MG');
INSERT INTO cities(name,state) VALUES ('São Bento do Sapucaí','SP');
INSERT INTO cities(name,state) VALUES ('Piranguinho','MG');
INSERT INTO cities(name,state) VALUES ('Caçapava','SP');
INSERT INTO cities(name,state) VALUES ('Jacareí','SP')


INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Transamérica SP','Transamérica',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Arquibancada','Rádio Arquibancada',3,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Bandeirantes SP','Rádio Bandeirantes',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Metropolitana FM SP','Metropolitana',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Band Vale FM','Band Vale',2,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio 105 FM','105 FM',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Mix FM SP','Mix FM',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('D2 FM','D2 FM',4,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Nova Brasil','Nova Brasil',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Energia 97 FM','Energia 97',1,'http://stream.com','http://image.com',false);

INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio CBN','CBN',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Jovem Pan','Joven Pan',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Gazeta FM','Rádio Gazeta',1,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio Melodia FM','Melodia FM',3,'http://stream.com','http://image.com',false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl) VALUES ('Rádio 93 FM','93FM',3,'http://stream.com','http://image.com',false);

INSERT INTO radio_category(radio_id,category_id) VALUES (1,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (1,3);
INSERT INTO radio_category(radio_id,category_id) VALUES (1,4);

INSERT INTO radio_category(radio_id,category_id) VALUES (2,8);

INSERT INTO radio_category(radio_id,category_id) VALUES (3,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (3,2);

INSERT INTO radio_category(radio_id,category_id) VALUES (4,4);

INSERT INTO radio_category(radio_id,category_id) VALUES (5,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (5,10);

INSERT INTO radio_category(radio_id,category_id) VALUES (6,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (6,9);
INSERT INTO radio_category(radio_id,category_id) VALUES (6,11);

INSERT INTO radio_category(radio_id,category_id) VALUES (7,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (7,5);

INSERT INTO radio_category(radio_id,category_id) VALUES (8,3);

INSERT INTO radio_category(radio_id,category_id) VALUES (9,10);

INSERT INTO radio_category(radio_id,category_id) VALUES (10,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (10,12);

INSERT INTO radio_category(radio_id,category_id) VALUES (11,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (12,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (13,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (14,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (15,1);

INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do JC',1,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do Alex',2,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista da Maria',3,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do JC - Futebol',1,Now(),Now());

INSERT INTO list_items(list_id,radio_id) VALUES (1,1);
INSERT INTO list_items(list_id,radio_id) VALUES (1,2);
INSERT INTO list_items(list_id,radio_id) VALUES (1,3);
INSERT INTO list_items(list_id,radio_id) VALUES (2,1);
INSERT INTO list_items(list_id,radio_id) VALUES (3,2);
INSERT INTO list_items(list_id,radio_id) VALUES (3,3);
