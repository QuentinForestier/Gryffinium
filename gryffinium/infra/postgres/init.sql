CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id       uuid DEFAULT uuid_generate_v4(),
    name     varchar(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS project
(
    id      uuid DEFAULT uuid_generate_v4(),
    name    varchar(255) NOT NULL,
    diagram xml,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS project_user
(
    project_id uuid    NOT NULL,
    user_id    uuid    NOT NULL,
    can_write   BOOLEAN NOT NULL,
    is_owner    BOOLEAN NOT NULL,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project_user_project_id FOREIGN KEY (project_id) REFERENCES project (id),
    CONSTRAINT fk_project_user_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);