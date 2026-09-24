CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'USER'))
);

ALTER TABLE snippets ADD COLUMN owner_id UUID;
ALTER TABLE snippets ADD COLUMN visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE';

ALTER TABLE snippets
    ADD CONSTRAINT fk_snippets_owner
        FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE snippets
    ADD CONSTRAINT chk_snippets_visibility
        CHECK (visibility IN ('PRIVATE', 'DEMO'));

CREATE INDEX idx_snippets_owner_updated_at ON snippets (owner_id, updated_at DESC);
CREATE INDEX idx_snippets_visibility_updated_at ON snippets (visibility, updated_at DESC);
