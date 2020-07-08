CREATE TABLE IF NOT EXISTS example.user (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    name VARCHAR(250) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

-- index on TEXT not supported by H2
CREATE INDEX IF NOT EXISTS user_name_idx ON example.user (name);
