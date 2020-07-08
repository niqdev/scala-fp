CREATE TABLE IF NOT EXISTS example.repository (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    user_id UUID NOT NULL,
    name VARCHAR(250) NOT NULL,
    url VARCHAR(250) NOT NULL,
    is_fork BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT repository_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS repository_user_id_idx ON example.repository (user_id);
CREATE INDEX IF NOT EXISTS repository_name_idx ON example.repository (name);

ALTER TABLE example.repository ADD FOREIGN KEY (user_id) REFERENCES example.user (id);
