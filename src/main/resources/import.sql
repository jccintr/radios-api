INSERT INTO users (name, email, password) VALUES ('Alex', 'alex@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Maria',  'maria@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');
INSERT INTO users (name, email, password) VALUES ('Julio Cesar',  'jc@gmail.com', '$2a$10$e1zv4PUeNpbrbRYpJmk0euJnt5xNI0PGcYMxrSX8t6kJrAmOZSS/S');

INSERT INTO roles (authority) VALUES ('ROLE_ADMIN');
INSERT INTO roles (authority) VALUES ('ROLE_COMMON');

INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 1);

INSERT INTO categories(name) VALUES ('Esportes'); 
INSERT INTO categories(name) VALUES ('Notícias');
INSERT INTO categories(name) VALUES ('Rock');
INSERT INTO categories(name) VALUES ('Pop');
INSERT INTO categories(name) VALUES ('Sertanejo');
INSERT INTO categories(name) VALUES ('Gospel');
INSERT INTO categories(name) VALUES ('Católica');
INSERT INTO categories(name) VALUES ('Carnaval');
INSERT INTO categories(name) VALUES ('Samba e Pagode');
INSERT INTO categories(name) VALUES ('MPB');
INSERT INTO categories(name) VALUES ('RAPP');
INSERT INTO categories(name) VALUES ('Dance');

INSERT INTO cities(name,state) VALUES ('São Paulo','SP');
INSERT INTO cities(name,state) VALUES ('Campos do Jordão','SP');
INSERT INTO cities(name,state) VALUES ('Rio de Janeiro','RJ');
INSERT INTO cities(name,state) VALUES ('Santa Rita do Sapucaí','MG');

INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Rádio Transamérica SP','Transamérica',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Rádio Arquibancada','Rádio Arquibancada',3,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Rádio Bandeirantes SP','Rádio Bandeirantes',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Metropolitana FM SP','Metropolitana',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Band Vale FM','Band Vale',2,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Rádio 105 FM','105 FM',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Mix FM SP','Mix FM',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('D2 FM','D2 FM',4,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Rádio Nova Brasil','Nova Brasil',1,'http://stream.com','http://image.com',false,false);
INSERT INTO radios (name,short_name,city_id,stream_url,image_url,hsl,web_radio) VALUES ('Energia 97 FM','Energia 97',1,'http://stream.com','http://image.com',false,false);

INSERT INTO radio_category(radio_id,category_id) VALUES (1,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (1,3);
INSERT INTO radio_category(radio_id,category_id) VALUES (1,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (2,8);
INSERT INTO radio_category(radio_id,category_id) VALUES (3,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (3,2);
INSERT INTO radio_category(radio_id,category_id) VALUES (4,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (5,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (5,10);
INSERT INTO radio_category(radio_id,category_id) VALUES (5,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (6,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (6,9);
INSERT INTO radio_category(radio_id,category_id) VALUES (6,11);
INSERT INTO radio_category(radio_id,category_id) VALUES (7,4);
INSERT INTO radio_category(radio_id,category_id) VALUES (7,5);
INSERT INTO radio_category(radio_id,category_id) VALUES (8,3);
INSERT INTO radio_category(radio_id,category_id) VALUES (9,10);
INSERT INTO radio_category(radio_id,category_id) VALUES (10,1);
INSERT INTO radio_category(radio_id,category_id) VALUES (10,12);

INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do JC',3,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do Alex',1,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista da Maria',2,Now(),Now());
INSERT INTO lists(name,user_id,created_at,updated_at) VALUES ('Lista do JC - Futebol',3,Now(),Now());

INSERT INTO list_items(list_id,radio_id) VALUES (1,1);
INSERT INTO list_items(list_id,radio_id) VALUES (1,2);
INSERT INTO list_items(list_id,radio_id) VALUES (1,3);
