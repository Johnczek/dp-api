INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user5WithoutAddress', 'user5', 'user5WithoutAddress@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account without address', now());

INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user6WithoutBankAccount', 'user6', 'user6WithoutBankAccount@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account without bank account', now());

INSERT INTO "user" (first_name, last_name, email, password, description, registered)
VALUES ('user7WithoutAddressAndBankAccount', 'user7', 'user7WithoutAddressAndBankAccount@user.com', '$2a$10$sNPUkDkJgbTmsxo0xrpCdO0cfaRr5e4WmnaGlLif3fOln2w3jEny6', 'user account without address and bank account', now());


INSERT INTO "address" (street, street_number, zipCode, city, user_id)
VALUES ('User 6 street', '1', '69632', 'Cityy', (SELECT u.id FROM "user" u WHERE u.email = 'user6WithoutBankAccount@user.com'));


INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user5WithoutAddress@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user6WithoutBankAccount@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));

INSERT INTO "user_role" (user_id, role_id)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user7WithoutAddressAndBankAccount@user.com'), (SELECT r.id FROM role r WHERE r.code = 'USER'));


INSERT INTO "bank_account" (prefix, number, bank_code, user_id)
VALUES (null, 1111112, 1010, (SELECT u.id FROM "user" u WHERE u.email = 'user5WithoutAddress@user.com'));
