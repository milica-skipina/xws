INSERT INTO entrepreneur(address, bin, company_name, name, surname) VALUES ('Stepe Stepanovica','123458363', 'RentACar', null, null);
INSERT INTO pricelist(discount20, discount30, discountdc, price_day) VALUES (20, 30, 10, 20);

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

-- AUTHORITY
INSERT INTO authority(name) VALUES ('ROLE_CUSTOMER');
INSERT INTO authority(name) VALUES ('ROLE_ADMIN');
INSERT INTO authority(name) VALUES ('ROLE_SELLER');


