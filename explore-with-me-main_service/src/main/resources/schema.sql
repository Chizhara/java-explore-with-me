CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(250) UNIQUE NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id BIGINT REFERENCES categories(id) NOT NULL,
    initiator_id BIGINT REFERENCES users(id) NOT NULL,
    location_lat REAL,
    location_lon REAL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    paid BOOLEAN,
    participant_limit INTEGER NULL,
    request_moderation BOOLEAN NULL,
    state VARCHAR(20) NULL,
    published_date TIMESTAMP WITHOUT TIME ZONE,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT REFERENCES users(id) NOT NULL,
    event_id BIGINT REFERENCES events(id) NOT NULL,
    status VARCHAR(10) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UNIQUE(requester_id, event_id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations(
    compilation_id BIGINT REFERENCES compilations(id) NOT NULL,
    event_id BIGINT REFERENCES events(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions(
    subscriber_id BIGINT REFERENCES users(id) NOT NULL,
    recipient_id BIGINT REFERENCES users(id) NOT NULL,
    PRIMARY KEY(subscriber_id, recipient_id),
    CHECK (subscriber_id <> recipient_id)
);


