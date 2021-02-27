CREATE TABLE IF NOT EXISTS "order"
(
    id        BIGSERIAL PRIMARY KEY,
    created   TIMESTAMP NOT NULL,
    buyer_id  BIGINT    NOT NULL,
    seller_id BIGINT    NOT NULL,
    item_id   BIGINT    NOT NULL,
    CONSTRAINT order__buyer_id__fk
        FOREIGN KEY (buyer_id)
            REFERENCES public.user (id),
    CONSTRAINT order__seller_id__fk
        FOREIGN KEY (seller_id)
            REFERENCES public.user (id),
    CONSTRAINT order__item_id__fk
        FOREIGN KEY (item_id)
            REFERENCES public.item (id)
);
