CREATE TABLE IF NOT EXISTS "user"
(
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(128) NOT NULL,
    last_name   VARCHAR(128) NOT NULL,
    email       VARCHAR(128) NOT NULL UNIQUE,
    avatar      VARCHAR(128) NOT NULL,
    password    VARCHAR(512) NOT NULL,
    description TEXT,
    registered  TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS "address"
(
    id            BIGSERIAL PRIMARY KEY,
    street        VARCHAR(128),
    street_number VARCHAR(128),
    zipCode       VARCHAR(128) NOT NULL,
    city          VARCHAR(128) NOT NULL,
    user_id       BIGINT       NOT NULL,
    CONSTRAINT address__user_id__fk
        FOREIGN KEY (user_id)
            REFERENCES public.user (id)
);

CREATE TABLE IF NOT EXISTS "role"
(
    id   BIGSERIAL PRIMARY KEY,
    code VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "user_role"
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT user_role__user_id__fk
        FOREIGN KEY (user_id)
            REFERENCES public.user (id),
    CONSTRAINT user_role__role_id__fk
        FOREIGN KEY (role_id)
            REFERENCES public.role (id)
);

CREATE TABLE IF NOT EXISTS "bank_account"
(
    id        BIGSERIAL PRIMARY KEY,
    prefix    INTEGER,
    number    INTEGER  NOT NULL,
    bank_code SMALLINT NOT NULL,
    user_id   BIGINT   NOT NULL,
    CONSTRAINT bank_account__user_id__fk
        FOREIGN KEY (user_id)
            REFERENCES public.user (id)
);
