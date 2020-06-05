-- AUTHORITY
INSERT INTO privilege(name) VALUES ('READ_CODE');
INSERT INTO privilege(name) VALUES ('WRITE_CODE');
INSERT INTO privilege(name) VALUES ('READ_CAR');
INSERT INTO privilege(name) VALUES ('READ_AD');
INSERT INTO privilege(name) VALUES ('WRITE_AD');
INSERT INTO privilege(name) VALUES ('MODIFY_AD');
INSERT INTO privilege(name) VALUES ('DELETE_AD');
INSERT INTO privilege(name) VALUES ('WRITE_PRICE');
INSERT INTO privilege(name) VALUES ('READ_PRICE');
INSERT INTO privilege(name) VALUES ('EDIT_CODE');
INSERT INTO privilege(name) VALUES ('DELETE_CODE');
INSERT INTO privilege(name) VALUES ('CAN_ACCESS');
INSERT INTO privilege(name) VALUES ('CREATE_REQUEST');
INSERT INTO privilege(name) VALUES ('READ_REQUEST');
INSERT INTO privilege(name) VALUES ('MODIFY_REQUEST');

INSERT INTO authority(name) VALUES ('ROLE_CUSTOMER');
INSERT INTO authority(name) VALUES ('ROLE_ADMIN');
INSERT INTO authority(name) VALUES ('ROLE_SELLER');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 13);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 14);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 2);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 8);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 9);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 9);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 14);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 10);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 11);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 15);


INSERT INTO korisnik (username, password, email, enabled, activated) VALUES ('prodavac', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'prodavac@gmail.com', true, true);
INSERT INTO entrepreneur(address, bin, company_name, name, surname, user_id) VALUES ('Stepe Stepanovica','123458363', 'RentACar', null, null, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 3);

INSERT INTO korisnik (username, password, email, enabled, activated) VALUES ('kupac', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'kupac@gmail.com', true, true);
INSERT INTO customer(address, city, name, surname, user_id) VALUES ('Stepe Stepanovica','Novi Sad', 'Marko', 'Markovic', 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);

INSERT INTO korisnik (username, password, email, enabled, activated) VALUES ('admin', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'admin@gmail.com', true, true);
INSERT INTO admin(user_id) VALUES (3);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);


INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (30, 30, 10, 20, 50, false);
INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (24, 12, 10, 20, 50, false);
INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (31, 150, 10, 20, 50, false);
INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (15, 20, 10, 20, 50, false);
INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (140, 40, 10, 20, 50, false);


INSERT INTO codebook(name, code, code_type, deleted) VALUES ('BMW', 'B-235', 'brand', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Audi', 'B-489', 'brand', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Mercedes', 'B-403', 'brand', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Tesla', 'B-304', 'brand', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Ostali', 'B-402', 'brand', false);

INSERT INTO codebook(name, code, code_type, deleted) VALUES ('M5', 'M-402', 'model', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('R8', 'M-458', 'model', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Ostali', 'M-965', 'model', false);

INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Benzin', 'F-785', 'fuel', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Plin', 'F-156', 'fuel', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Dizel', 'F-197', 'fuel', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Ostali', 'F-248', 'fuel', false);

INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Manuelni', 'G-123', 'gearbox', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Automatski', 'G-687', 'gearbox', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Poluautomatski', 'G-986', 'gearbox', false);

INSERT INTO codebook(name, code, code_type, deleted) VALUES ('SUV', 'C-785', 'class', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Old tajmer', 'C-427', 'class', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Gradski', 'C-852', 'class', false);
INSERT INTO codebook(name, code, code_type, deleted) VALUES ('Ostali', 'C-986', 'class', false);




