package com.olivier.devhub.snippet.service;

import java.util.UUID;

public class SnippetNotFoundException extends RuntimeException {

    public SnippetNotFoundException(UUID id) {
        super("No snippet exists with id '%s'.".formatted(id));
    }
}
