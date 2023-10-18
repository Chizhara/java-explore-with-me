CREATE TABLE IF NOT EXISTS endpoint_hits(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(50) NOT NULL,
    uri VARCHAR(150) NOT NULL,
    ip VARCHAR(40) NOT NULL,
    hit_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);