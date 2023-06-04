---------------------------
-- Create the Table      --
---------------------------
--      ACCOUNTS
CREATE TABLE ACCOUNTS
(
    id         UUID               NOT NULL
                         DEFAULT random_uuid() PRIMARY KEY,

    email      VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(500)       NOT NULL,

    user_name  VARCHAR(50)        NOT NULL,

    created_at TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at  TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted    BOOLEAN   DEFAULT FALSE
);

--      POST
CREATE TABLE POST
(
    id          UUID         NOT NULL
                          DEFAULT random_uuid() PRIMARY KEY,

    header_post VARCHAR(255) NOT NULL,
    text_post   VARCHAR(500) NOT NULL,
    image_post  VARCHAR(255),

    author_id   UUID         NOT NULL REFERENCES ACCOUNTS (id),
    created_at  TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at   TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted     BOOLEAN   DEFAULT FALSE
);

--      REQUEST FRIENDSHIP
CREATE TABLE REQUEST_FRIENDSHIP
(
    id              UUID NOT NULL
                              DEFAULT random_uuid() PRIMARY KEY,

    account_from_id UUID NOT NULL REFERENCES ACCOUNTS (id),
    account_to_id   UUID NOT NULL REFERENCES ACCOUNTS (id) NOT NULL,
    status          VARCHAR(255),

    created_at      TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at       TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted         BOOLEAN   DEFAULT FALSE
);

--      FRIENDS
CREATE TABLE FRIENDS
(
    id              UUID NOT NULL
                              DEFAULT random_uuid() PRIMARY KEY,

    account_from_id UUID NOT NULL REFERENCES ACCOUNTS (id),
    account_to_id   UUID NOT NULL REFERENCES ACCOUNTS (id) NOT NULL,

    created_at      TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at       TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted         BOOLEAN   DEFAULT FALSE
);

--      SUBSCRIBERS
CREATE TABLE SUBSCRIBERS
(
    id              UUID NOT NULL
                              DEFAULT random_uuid() PRIMARY KEY,

    account_from_id UUID NOT NULL REFERENCES ACCOUNTS (id),
    account_to_id   UUID NOT NULL REFERENCES ACCOUNTS (id),

    created_at      TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at       TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted         BOOLEAN   DEFAULT FALSE
);

--      CHAT
CREATE TABLE CHAT
(
    id              UUID NOT NULL
                              DEFAULT random_uuid() PRIMARY KEY,

    account_from_id UUID NOT NULL REFERENCES ACCOUNTS (id),
    account_to_id   UUID NOT NULL REFERENCES ACCOUNTS (id),
    message         VARCHAR(500),

    created_at      TIMESTAMP DEFAULT current_TIMESTAMP,
    update_at       TIMESTAMP DEFAULT current_TIMESTAMP,
    deleted         BOOLEAN   DEFAULT FALSE
);