INSERT INTO "user" (first_name, last_name, email, avatar, password, description, registered)
VALUES ('user', 'user', 'user@user.com', '/images/0.png', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account', now());
INSERT INTO "user" (first_name, last_name, email, avatar, password, description, registered)
VALUES ('admin', 'admin', 'admin@admin.com', '/images/1.png', '$2a$10$TERoy6xkUAg9URgexUtf4uapxFobh04UXR/XY8Ke0GRxdVxxJZxOu', 'admin account', now());

INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('User street', '1', '69632', 'Ždánice', (SELECT u.id FROM "user" u WHERE u.email = 'user@user.com'));
INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('Admin street', '1', '69632', 'Ždánice', (SELECT u.id FROM "user" u WHERE u.email = 'admin@admin.com'));

INSERT INTO "role" (code) VALUES ('USER');
INSERT INTO "role" (code) VALUES ('ADMIN');

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));
INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'admin@admin.com'), (SELECT r.id FROM role r WHERE r.code = 'ADMIN'));

INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 111111, 1010, (SELECT u.id FROM "user" u WHERE u.email = 'user@user.com'));
INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 22222222, 2222, (SELECT u.id FROM "user" u WHERE u.email = 'admin@admin.com'));