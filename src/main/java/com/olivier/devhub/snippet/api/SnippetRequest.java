package com.olivier.devhub.snippet.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SnippetRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank String content,
        @NotBlank @Size(max = 50) String language,
        @Size(max = 1_000) String description,
        @Size(max = 20) Set<@NotBlank @Size(max = 100) String> tags
) {
}
