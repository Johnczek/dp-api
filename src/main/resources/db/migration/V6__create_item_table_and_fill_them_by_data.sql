CREATE TABLE IF NOT EXISTS "item"
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(256)    NOT NULL,
    description    TEXT,
    state          VARCHAR(128)    NOT NULL,
    valid_from     TIMESTAMP       NOT NULL,
    valid_to       TIMESTAMP       NOT NULL,
    topped         BOOLEAN,
    starting_price DECIMAL(100, 2) NOT NULL,
    picture_id     BIGINT          NOT NULL,
    payment_id     BIGINT          NOT NULL,
    delivery_id    BIGINT          NOT NULL,
    seller_id      BIGINT          NOT NULL,
    CONSTRAINT item__picture_id__fk
        FOREIGN KEY (picture_id)
            REFERENCES public.file (id),
    CONSTRAINT item__payment_id__fk
        FOREIGN KEY (payment_id)
            REFERENCES public.payment (id),
    CONSTRAINT item__delivery_id__fk
        FOREIGN KEY (delivery_id)
            REFERENCES public.delivery (id),
    CONSTRAINT item__seller_id__fk
        FOREIGN KEY (seller_id)
            REFERENCES public."user" (id)

);

INSERT INTO "file" (file_identifier, file_extension, type)
VALUES ('3cedb358-58de-11eb-ae93-0242ac130002', 'png', 'ITEM_PICTURE'); -- item 1 picture
INSERT INTO "file" (file_identifier, file_extension, type)
VALUES ('9f4b9c54-58de-11eb-ae93-0242ac130002', 'png', 'ITEM_PICTURE'); -- item 2 picture
INSERT INTO "file" (file_identifier, file_extension, type)
VALUES ('9f4b9eb6-58de-11eb-ae93-0242ac130002', 'png', 'ITEM_PICTURE'); -- item 3 picture
INSERT INTO "file" (file_identifier, file_extension, type)
VALUES ('9f4ba262-58de-11eb-ae93-0242ac130002', 'png', 'ITEM_PICTURE'); -- item 4 picture

INSERT INTO "item" (name, description, state, valid_from, valid_to, topped, starting_price, picture_id, payment_id,
                    delivery_id, seller_id)
VALUES ('Item 1', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque orci lectus, mattis sed posuere sed, cursus at augue. Phasellus est odio, rhoncus vel dui sit amet, commodo tincidunt tortor. Etiam consectetur ante a tempor aliquam. Morbi vel volutpat nibh, eget accumsan mi. Cras sollicitudin consectetur dictum. Morbi id egestas eros, non sodales quam. Vestibulum erat est, feugiat id fermentum sit amet, gravida quis ligula. Curabitur quis leo nunc.

Suspendisse potenti. Nunc mi velit, varius ac sodales id, maximus sit amet augue. Etiam tortor nunc, accumsan eu quam porta, blandit lacinia nibh. Vivamus aliquet quam magna, non vulputate mauris elementum sit amet. Cras finibus ultricies augue. Donec non tellus eu justo vestibulum efficitur. Vivamus laoreet sem ut pellentesque commodo.',
        'ACTIVE', now() - INTERVAL '1 day', now() + INTERVAL '1 year', true, 100,
        (SELECT id FROM file WHERE file_identifier = '3cedb358-58de-11eb-ae93-0242ac130002'),
        (SELECT id FROM payment WHERE id = 1), (SELECT id FROM delivery WHERE id = 1),
        (SELECT u.id FROM "user" u WHERE u.email = 'user@user.com'));

INSERT INTO "item" (name, description, state, valid_from, valid_to, topped, starting_price, picture_id, payment_id,
                    delivery_id, seller_id)
VALUES ('Item 2', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque orci lectus, mattis sed posuere sed, cursus at augue. Phasellus est odio, rhoncus vel dui sit amet, commodo tincidunt tortor. Etiam consectetur ante a tempor aliquam. Morbi vel volutpat nibh, eget accumsan mi. Cras sollicitudin consectetur dictum. Morbi id egestas eros, non sodales quam. Vestibulum erat est, feugiat id fermentum sit amet, gravida quis ligula. Curabitur quis leo nunc.

Suspendisse potenti. Nunc mi velit, varius ac sodales id, maximus sit amet augue. Etiam tortor nunc, accumsan eu quam porta, blandit lacinia nibh. Vivamus aliquet quam magna, non vulputate mauris elementum sit amet. Cras finibus ultricies augue. Donec non tellus eu justo vestibulum efficitur. Vivamus laoreet sem ut pellentesque commodo.

Vivamus quis consequat ligula, id posuere nunc. Cras tempus dui ac velit condimentum, a sagittis libero sodales. Morbi at enim tincidunt, scelerisque sapien quis, dignissim eros. Sed tristique scelerisque tellus, et consectetur velit. Duis tempor metus eget purus luctus, id faucibus ex auctor. Nulla lacinia ipsum urna, id sollicitudin felis efficitur non. Phasellus maximus tempus eros, eget mollis libero hendrerit sed. Curabitur dignissim, libero eget facilisis ullamcorper, mi nibh viverra massa, eleifend fringilla tortor turpis mattis augue. Sed et gravida augue. Vestibulum id lacinia nunc, sit amet porttitor leo. Maecenas finibus, mauris a egestas tincidunt, nibh lorem condimentum felis, vitae gravida lorem magna id risus. Mauris dapibus volutpat ultricies.

Suspendisse porta mi ac dui eleifend porttitor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ut ligula eu nulla vestibulum commodo. Curabitur varius cursus est, a scelerisque dolor tristique in. Donec et mi at odio elementum tristique in a sem. Duis id ornare magna. Sed ac laoreet enim. Mauris ultrices, lectus a venenatis dapibus, arcu ligula varius lorem, nec accumsan lacus sem sed dolor. Cras imperdiet dignissim tellus quis vehicula. Duis cursus, lorem quis luctus fringilla, tortor turpis egestas nunc, consectetur accumsan odio neque at sem. Sed finibus felis vel elit rutrum pretium. Duis lectus lorem, efficitur eu nulla eget, pellentesque rhoncus metus. Fusce fermentum aliquam mattis. Pellentesque sed est at lectus viverra pretium quis vel est. Vestibulum vel dignissim augue.',
        'ACTIVE', now(), now() + INTERVAL '1 year', true, 1,
        (SELECT id FROM file WHERE file_identifier = '9f4b9c54-58de-11eb-ae93-0242ac130002'),
        (SELECT id FROM payment WHERE id = 2), (SELECT id FROM delivery WHERE id = 1),
        (SELECT u.id FROM "user" u WHERE u.email = 'user@user.com'));

INSERT INTO "item" (name, description, state, valid_from, valid_to, topped, starting_price, picture_id, payment_id,
                    delivery_id, seller_id)
VALUES ('Item 3', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
        'ACTIVE', now(), now() + INTERVAL '1 year', true, 30000,
        (SELECT id FROM file WHERE file_identifier = '9f4b9eb6-58de-11eb-ae93-0242ac130002'),
        (SELECT id FROM payment WHERE id = 2), (SELECT id FROM delivery WHERE id = 2),
        (SELECT u.id FROM "user" u WHERE u.email = 'user2@user.com'));

INSERT INTO "item" (name, description, state, valid_from, valid_to, topped, starting_price, picture_id, payment_id,
                    delivery_id, seller_id)
VALUES ('Item 4',
        'Nam nec fermentum enim, in pretium arcu. Nunc euismod ante quis tempus hendrerit. Ut dolor turpis, lacinia eu tellus a, consequat finibus nulla. Vestibulum at justo nec nunc elementum scelerisque. Sed non rhoncus mauris. In hac habitasse platea dictumst. In neque libero, mattis a eros id, sagittis commodo tortor. Nam non nibh sed magna finibus maximus non quis sem. Nam non ultricies libero, id luctus turpis.',
        'ACTIVE', now(), now() + INTERVAL '1 year', true, 23,
        (SELECT id FROM file WHERE file_identifier = '9f4ba262-58de-11eb-ae93-0242ac130002'),
        (SELECT id FROM payment WHERE id = 1), (SELECT id FROM delivery WHERE id = 2),
        (SELECT u.id FROM "user" u WHERE u.email = 'user3@user.com'));

CREATE TABLE IF NOT EXISTS "item_bid"
(
    id       BIGSERIAL PRIMARY KEY,
    buyer_id BIGINT         NOT NULL,
    item_id  BIGINT         NOT NULL,
    amount   DECIMAL(10, 2) NOT NULL,
    time     TIMESTAMP      NOT NULL,
    CONSTRAINT item_bid__buyer_id__fk
        FOREIGN KEY (buyer_id)
            REFERENCES public.user (id),
    CONSTRAINT item_bid__item_id__fk
        FOREIGN KEY (item_id)
            REFERENCES public.item (id)
);

INSERT INTO "item_bid" (buyer_id, item_id, amount, time)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user4@user.com'), (SELECT id FROM "item" WHERE name = 'Item 1'),
        101, now() - INTERVAL '5 minutes');
INSERT INTO "item_bid" (buyer_id, item_id, amount, time)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user3@user.com'), (SELECT id FROM "item" WHERE name = 'Item 1'),
        105, now() - INTERVAL '3 minutes');
INSERT INTO "item_bid" (buyer_id, item_id, amount, time)
VALUES ((SELECT u.id FROM "user" u WHERE u.email = 'user4@user.com'), (SELECT id FROM "item" WHERE name = 'Item 1'),
        106, now());

