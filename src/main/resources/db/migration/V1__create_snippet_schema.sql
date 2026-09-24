CREATE TABLE snippets (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    language VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tags (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE snippet_tags (
    snippet_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (snippet_id, tag_id),
    CONSTRAINT fk_snippet_tags_snippet
        FOREIGN KEY (snippet_id) REFERENCES snippets (id) ON DELETE CASCADE,
    CONSTRAINT fk_snippet_tags_tag
        FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE INDEX idx_snippets_language ON snippets (language);
CREATE INDEX idx_tags_name ON tags (name);
CREATE INDEX idx_snippet_tags_tag_id ON snippet_tags (tag_id);
