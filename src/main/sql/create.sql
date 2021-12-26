DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE users (
    id                      SERIAL PRIMARY KEY,
    email                   TEXT    NOT NULL UNIQUE,
    username                TEXT    NOT NULL UNIQUE,
    password                TEXT    NOT NULL,
    enabled                 BOOLEAN NOT NULL,
    token                   TEXT,
    non_expired             BOOLEAN NOT NULL,
    non_locked              BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL
);

CREATE TABLE groups (
    id       SERIAL PRIMARY KEY,
    name     TEXT NOT NULL,
    admin_id INT  NOT NULL REFERENCES users (id)
);

CREATE TABLE user_in_group (
    user_id  INT REFERENCES users (id),
    group_id INT REFERENCES groups (id),
    PRIMARY KEY (user_id, group_id)
);

CREATE TYPE debt_status AS ENUM (
    'REQUESTED',
    'ACTIVE',
    'PAID'
    );

CREATE TABLE debt (
    id          SERIAL PRIMARY KEY,
    group_id    INT         NOT NULL REFERENCES groups (id),
    amount      INT         NOT NULL CHECK (amount > 0),
    lender_id   INT         NOT NULL REFERENCES users (id),
    loaner_id   INT         NOT NULL REFERENCES users (id),
    description TEXT,
    status      debt_status NOT NULL,
    CHECK (lender_id != loaner_id)
);
