package com.olivier.devhub.snippet.api;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record SnippetResponse(
        UUID id,
        String title,
        String content,
        String language,
        String description,
        Set<String> tags,
        Instant createdAt,
        Instant updatedAt
) {
}
