INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user2', 'user2', 'user2@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account', now());

INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user3', 'user3', 'user3@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account', now());

INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user4', 'user4', 'user4@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account', now());


INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('User 2 street', '1', '69632', 'Ždánice', (SELECT u.id FROM "user" u WHERE u.email = 'user2@user.com'));

INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('User 3 street', '1', '69632', 'Ždánice', (SELECT u.id FROM "user" u WHERE u.email = 'user3@user.com'));

INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('User 4 street', '1', '69632', 'Ždánice', (SELECT u.id FROM "user" u WHERE u.email = 'user4@user.com'));

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user2@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user3@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user4@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));


INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 1111112, 1010, (SELECT u.id FROM "user" u WHERE u.email = 'user2@user.com'));

INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 1111113, 1010, (SELECT u.id FROM "user" u WHERE u.email = 'user3@user.com'));

INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 1111114, 1010, (SELECT u.id FROM "user" u WHERE u.email = 'user4@user.com'));