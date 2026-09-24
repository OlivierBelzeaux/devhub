package com.olivier.devhub.snippet.api;

import org.springframework.data.domain.Page;

import java.util.List;

public record SnippetPageResponse(
        List<SnippetResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static SnippetPageResponse from(Page<SnippetResponse> snippets) {
        return new SnippetPageResponse(
                snippets.getContent(),
                snippets.getNumber(),
                snippets.getSize(),
                snippets.getTotalElements(),
                snippets.getTotalPages(),
                snippets.isFirst(),
                snippets.isLast()
        );
    }
}
