package com.olivier.devhub.snippet.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SnippetRequest(
        @Schema(description = "Short title that identifies the snippet", example = "PostgreSQL health check")
        @NotBlank @Size(min = 1, max = 255) String title,
        @Schema(description = "Code or configuration to store", example = "GET /actuator/health")
        @NotBlank String content,
        @Schema(description = "Programming or configuration language", example = "http")
        @NotBlank @Size(min = 1, max = 50) String language,
        @Schema(description = "Optional explanation", example = "Checks whether the application is available.")
        @Size(max = 1_000) String description,
        @Schema(description = "Optional labels used to organize snippets", example = "[\"spring\", \"actuator\"]")
        @Size(max = 20) Set<@NotBlank @Size(min = 1, max = 100) String> tags
) {
}
