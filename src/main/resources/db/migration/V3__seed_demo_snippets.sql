INSERT INTO snippets (id, title, content, language, description, visibility)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Spring Boot health check',
    'GET /actuator/health',
    'http',
    'Verifies that the application and its database are available.',
    'DEMO'
);

INSERT INTO tags (id, name) VALUES
    ('00000000-0000-0000-0000-000000000011', 'actuator'),
    ('00000000-0000-0000-0000-000000000012', 'spring-boot')
ON CONFLICT (name) DO NOTHING;

INSERT INTO snippet_tags (snippet_id, tag_id)
SELECT '00000000-0000-0000-0000-000000000001', id
FROM tags
WHERE name IN ('actuator', 'spring-boot');
