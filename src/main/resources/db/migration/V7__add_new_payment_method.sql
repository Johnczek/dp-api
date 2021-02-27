INSERT INTO "file" (file_identifier, file_extension, type) VALUES ('0a40738c-7909-11eb-9439-0242ac130002', 'png', 'PAYMENT_LOGO'); -- bank transfer logo

INSERT INTO "payment" (name, price, description, logo_id) VALUES ('Platba bankovním převodem', 0, null, (SELECT id FROM "file" WHERE file_identifier = '0a40738c-7909-11eb-9439-0242ac130002'));
