INSERT INTO privilege(name) VALUES ('READ_CODE');
INSERT INTO privilege(name) VALUES ('WRITE_CODE');
INSERT INTO privilege(name) VALUES ('READ_CAR');
INSERT INTO privilege(name) VALUES ('READ_AD');
INSERT INTO privilege(name) VALUES ('WRITE_AD');
INSERT INTO privilege(name) VALUES ('MODIFY_AD');
INSERT INTO privilege(name) VALUES ('DELETE_AD');
INSERT INTO privilege(name) VALUES ('WRITE_PRICE');
INSERT INTO privilege(name) VALUES ('READ_PRICE');
INSERT INTO privilege(name) VALUES ('ACCEPT_USER');

INSERT INTO authority(name) VALUES ('ROLE_CUSTOMER');
INSERT INTO authority(name) VALUES ('ROLE_ADMIN');
INSERT INTO authority(name) VALUES ('ROLE_SELLER');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 4);

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
INSERT INTO end_user(address, city, name, surname, user_id) VALUES ('Stepe Stepanovica','Novi Sad', 'Marko', 'Markovic', 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);

INSERT INTO table_user (username, password, email, enabled, activated) VALUES ('admin', '$2a$10$btfhHvv9/VumsTS2ZNnKOudQWzAqNURShHLU1Z3fIsFnpym1dxd.G', 'admin@gmail.com', true, true);
INSERT INTO table_admin(user_id) VALUES (3);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);