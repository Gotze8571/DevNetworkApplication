CREATE TABLE users (
    id           UUID PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL
);
