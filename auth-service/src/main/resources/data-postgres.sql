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
INSERT INTO privilege(name) VALUES ('READ_USER');
INSERT INTO privilege(name) VALUES ('MODIFY_USER');
INSERT INTO privilege(name) VALUES ('DELETE_USER');
INSERT INTO privilege(name) VALUES ('BLOCK_END_USER');
INSERT INTO privilege(name) VALUES ('SEND_SOAP');
INSERT INTO privilege(name) VALUES ('CREATE_MESSAGE');
INSERT INTO privilege(name) VALUES ('EDIT_PRICE');

INSERT INTO authority(name) VALUES ('ROLE_CUSTOMER');
INSERT INTO authority(name) VALUES ('ROLE_ADMIN');
INSERT INTO authority(name) VALUES ('ROLE_SELLER');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 13);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 14);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 15);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 21);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 27);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 9);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 2);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 8);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 9);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 10);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 12);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 22);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 23);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 24);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 25);

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 9);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 13);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 21);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 19);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 26);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 14);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 15);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 20);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 27);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 28);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('prodavac2', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'prodavac2@gmail.com', true, true, 0,false,false);
INSERT INTO agent(city, address, registry_number, company_name, name, surname, user_id) VALUES ('Beograd', 'Stepe Stepanovica','123458363', 'RentACar', null, null, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 3);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('kupac', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'kupac@gmail.com', true, true, 0,false,false);
INSERT INTO end_user(address, city, name, surname, user_id, first_login,can_comment,can_reserve,number_canceled_request,number_refused_comments) VALUES ('Stepe Stepanovica','Novi Sad', 'Marko', 'Markovic', 2, false,true,true,0,0);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('admin', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'admin@gmail.com', true, true, 0,false,false);
INSERT INTO table_admin(user_id) VALUES (3);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('djoda', '$2a$10$PkDHt3T9ussutTLpXWGXCOWRjIC5MVtT6dUkz25m7mvrl1u.1Ta4y', 'djordje.petrovic567@gmail.com', true, false, 0,false,false);
INSERT INTO table_admin(user_id) VALUES (4);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('kupac2', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'kupac2@gmail.com', true, true, 0,false,false);
INSERT INTO end_user(address, city, name, surname, user_id, first_login,can_comment,can_reserve,number_canceled_request,number_refused_comments) VALUES ('Stepe Stepanovica','Novi Sad', 'Petar', 'Petrovic', 5, false,true,true,0,0);
INSERT INTO user_authority (user_id, authority_id) VALUES (5, 1);

INSERT INTO table_user (username, password, email, enabled, activated, number_failed_login,deleted,blocked) VALUES ('kupac3', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'kupac3@gmail.com', true, true, 0,false,false);
INSERT INTO end_user(address, city, name, surname, user_id, first_login,can_comment,can_reserve,number_canceled_request,number_refused_comments) VALUES ('Stepe Stepanovica','Novi Sad', 'Jovan', 'Jovanovic', 6, false,true,true,0,0);
INSERT INTO user_authority (user_id, authority_id) VALUES (6, 1);
