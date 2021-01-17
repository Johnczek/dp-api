CREATE TABLE IF NOT EXISTS "file"
(
    id        BIGSERIAL PRIMARY KEY,
    file_identifier VARCHAR(256) NOT NULL,
    file_extension VARCHAR(16) NOT NULL,
    type      VARCHAR(128) NOT NULL
);


ALTER TABLE "user"
    ADD COLUMN IF NOT EXISTS avatar_id BIGINT;
ALTER TABLE "user"
    ADD CONSTRAINT fk_avatar_id
        FOREIGN KEY (avatar_id)
            REFERENCES file (id);

ALTER TABLE "user"
    DROP COLUMN IF EXISTS avatar;
