CREATE TABLE USERS
(
    id                  UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    first_name          VARCHAR(255) NOT NULL,
    last_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    profile_picture_url VARCHAR(255) NOT NULL,
    username            VARCHAR(255) NOT NULL,
    is_deleted          boolean,
    keycloak_id         UUID UNIQUE,
    created_at          timestamp    not null default NOW()
);

CREATE TABLE ORGANISATIONS
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    creator_id UUID         NOT NULL,
    CONSTRAINT fk_creator_id
        FOREIGN KEY (creator_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE PROJECTS
(
    id              UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    organisation_id UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    created_at      timestamp    not null default NOW(),
    CONSTRAINT fk_organisation_id
        FOREIGN KEY (organisation_id) REFERENCES ORGANISATIONS (id) ON DELETE CASCADE
);