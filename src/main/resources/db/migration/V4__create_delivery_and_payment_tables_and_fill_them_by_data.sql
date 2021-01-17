CREATE TABLE IF NOT EXISTS "payment"
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(256)    NOT NULL,
    price       DECIMAL(4, 2) NOT NULL,
    description TEXT,
    logo_id       BIGINT       NOT NULL,
    CONSTRAINT payment__logo_id__fk
        FOREIGN KEY (logo_id)
            REFERENCES public.file (id)
);

INSERT INTO "file" (file_identifier, file_extension, type) VALUES ('ba56d4b0-58d8-11eb-ae93-0242ac130002', 'png', 'PAYMENT_LOGO'); -- cash logo
INSERT INTO "file" (file_identifier, file_extension, type) VALUES ('14d56000-58d9-11eb-ae93-0242ac130002', 'png', 'PAYMENT_LOGO'); -- card logo

INSERT INTO "payment" (name, price, description, logo_id) VALUES ('Hotově při převzetí', 0, null, (SELECT id FROM "file" WHERE file_identifier = 'ba56d4b0-58d8-11eb-ae93-0242ac130002'));
INSERT INTO "payment" (name, price, description, logo_id) VALUES ('Kartou', 10, 'Pro platbu lze použít všechny běžně používané kreditní i debetní karty', (SELECT id FROM "file" WHERE file_identifier = '14d56000-58d9-11eb-ae93-0242ac130002'));

--------------------------------------------------------

CREATE TABLE IF NOT EXISTS "delivery"
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(256)    NOT NULL,
    price       DECIMAL(4, 2) NOT NULL,
    description TEXT,
    logo_id       BIGINT       NOT NULL,
    CONSTRAINT delivery__logo_id__fk
        FOREIGN KEY (logo_id)
            REFERENCES public.file (id)
);

INSERT INTO "file" (file_identifier, file_extension, type) VALUES ('ce9222c6-58d9-11eb-ae93-0242ac130002', 'png', 'DELIVERY_LOGO'); -- shipping logo
INSERT INTO "file" (file_identifier, file_extension, type) VALUES ('db8af6ce-58d9-11eb-ae93-0242ac130002', 'png', 'DELIVERY_LOGO'); -- pick up logo

INSERT INTO "delivery" (name, price, description, logo_id) VALUES ('Přepravní službou', 59, 'Pro doručení za první dveře', (SELECT id FROM "file" WHERE file_identifier = 'ce9222c6-58d9-11eb-ae93-0242ac130002'));
INSERT INTO "delivery" (name, price, description, logo_id) VALUES ('Osobní převzetí', 0, null, (SELECT id FROM "file" WHERE file_identifier = 'db8af6ce-58d9-11eb-ae93-0242ac130002'));