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

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 4);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 5);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 6);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 7);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (3, 9);


INSERT INTO table_user (username, password, email, enabled, activated) VALUES ('prodavac', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'prodavac@gmail.com', true, true);
INSERT INTO agent(city, address, registry_number, company_name, name, surname, user_id) VALUES ('Beograd', 'Stepe Stepanovica','123458363', 'RentACar', null, null, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 3);

INSERT INTO table_user (username, password, email, enabled, activated) VALUES ('kupac', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'kupac@gmail.com', true, true);
INSERT INTO end_user(address, city, name, surname, user_id, first_login) VALUES ('Stepe Stepanovica','Novi Sad', 'Marko', 'Markovic', 2, false);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);

INSERT INTO table_user (username, password, email, enabled, activated) VALUES ('admin', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'admin@gmail.com', true, true);
INSERT INTO table_admin(user_id) VALUES (3);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);

INSERT INTO table_user (username, password, email, enabled, activated) VALUES ('djoda', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'djordje.petrovic567@gmail.com', true, false);
INSERT INTO table_admin(user_id) VALUES (4);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);