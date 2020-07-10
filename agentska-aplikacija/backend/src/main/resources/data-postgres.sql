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
INSERT INTO privilege(name) VALUES ('CREATE_REPORT');
INSERT INTO privilege(name) VALUES ('CREATE_REVIEW');
INSERT INTO privilege(name) VALUES ('MODIFY_REVIEW');
INSERT INTO privilege(name) VALUES ('READ_PROFILE');
INSERT INTO privilege(name) VALUES ('READ_STATISTICS');
INSERT INTO privilege(name) VALUES ('READ_CUSTOMERS');
INSERT INTO privilege(name) VALUES ('EDIT_PRICE');
INSERT INTO privilege(name) VALUES ('MODIFY_USER');
INSERT INTO privilege(name) VALUES ('READ_USER');
INSERT INTO privilege(name) VALUES ('DELETE_USER');
INSERT INTO privilege(name) VALUES ('CREATE_MESSAGE');
INSERT INTO privilege(name) VALUES ('BLOCK_END_USER');



INSERT INTO authority(name) VALUES ('ROLE_CUSTOMER');
INSERT INTO authority(name) VALUES ('ROLE_ADMIN');
INSERT INTO authority(name) VALUES ('ROLE_SELLER');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 13);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 14);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 17);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 26);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 2);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 8);
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
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 18);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 22);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 24);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 25);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 23);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 26);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 15);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 16);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 20);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 21);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 22);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 26);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 27);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 15);



INSERT INTO korisnik (username, password, email, enabled,deleted, number_failed_login,blocked) VALUES ('prodavac', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'prodavac@gmail.com', true,false, 0,false);
INSERT INTO entrepreneur(address, bin, company_name, name, surname, user_id) VALUES ('Stepe Stepanovica','123458363', 'RentACar', null, null, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 3);

INSERT INTO korisnik (username, password, email, enabled,deleted, number_failed_login,blocked) VALUES ('kupac', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'kupac@gmail.com', true,false, 0,false);
INSERT INTO customer(address, city, name, surname, user_id, activated, first_login,can_comment,can_reserve,number_canceled_request,number_refused_comments) VALUES ('Stepe Stepanovica','Novi Sad', 'Marko', 'Markovic', 2, true, false,true,true,0,0);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);

INSERT INTO korisnik (username, password, email, enabled,deleted, number_failed_login,blocked) VALUES ('admin', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'admin@gmail.com', true,false, 0,false);
INSERT INTO admin(user_id) VALUES (3);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);

INSERT INTO korisnik (username, password, email, enabled,deleted, number_failed_login,blocked) VALUES ('kupac1', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'kupac1@gmail.com', true,false, 0,false);
INSERT INTO customer(address, city, name, surname, user_id, activated, first_login,can_comment,can_reserve,number_canceled_request,number_refused_comments) VALUES ('Vojvodjanska','Novi Sad', 'Dara', 'Bubamara', 4, false, false, true,true,0,0);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 1);


INSERT INTO pricelist(collisiondw, discount20, discount30, exceed_mileage, price_day, deleted) VALUES (30, 30, 10, 20, 50, false);


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



