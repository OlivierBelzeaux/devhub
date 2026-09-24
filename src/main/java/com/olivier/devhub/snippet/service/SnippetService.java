package com.olivier.devhub.snippet.service;

import com.olivier.devhub.snippet.api.SnippetRequest;
import com.olivier.devhub.snippet.api.SnippetResponse;
import com.olivier.devhub.snippet.domain.Snippet;
import com.olivier.devhub.snippet.domain.Tag;
import com.olivier.devhub.snippet.repository.SnippetRepository;
import com.olivier.devhub.snippet.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final TagRepository tagRepository;

    public SnippetService(SnippetRepository snippetRepository, TagRepository tagRepository) {
        this.snippetRepository = snippetRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public Collection<SnippetResponse> findAll() {
        return snippetRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SnippetResponse findById(UUID id) {
        return toResponse(findSnippet(id));
    }

    @Transactional
    public SnippetResponse create(SnippetRequest request) {
        Snippet snippet = new Snippet(
                UUID.randomUUID(),
                request.title().trim(),
                request.content(),
                request.language().trim().toLowerCase(Locale.ROOT),
                normalizeDescription(request.description()),
                resolveTags(request.tags())
        );
        return toResponse(snippetRepository.saveAndFlush(snippet));
    }

    @Transactional
    public SnippetResponse update(UUID id, SnippetRequest request) {
        Snippet snippet = findSnippet(id);
        snippet.update(
                request.title().trim(),
                request.content(),
                request.language().trim().toLowerCase(Locale.ROOT),
                normalizeDescription(request.description()),
                resolveTags(request.tags())
        );
        return toResponse(snippetRepository.saveAndFlush(snippet));
    }

    @Transactional
    public void delete(UUID id) {
        snippetRepository.delete(findSnippet(id));
    }

    private Snippet findSnippet(UUID id) {
        return snippetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found"));
    }

    private Set<Tag> resolveTags(Set<String> requestTags) {
        Set<String> names = new TreeSet<>();
        if (requestTags != null) {
            requestTags.stream()
                    .map(tag -> tag.trim().toLowerCase(Locale.ROOT))
                    .forEach(names::add);
        }

        Set<Tag> tags = new HashSet<>(tagRepository.findByNameIn(names));
        Set<String> existingNames = tags.stream().map(Tag::getName).collect(java.util.stream.Collectors.toSet());
        List<Tag> newTags = names.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> new Tag(UUID.randomUUID(), name))
                .toList();
        tags.addAll(tagRepository.saveAll(newTags));
        return tags;
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description.trim();
    }

    private SnippetResponse toResponse(Snippet snippet) {
        Set<String> tagNames = snippet.getTags().stream()
                .map(Tag::getName)
                .collect(java.util.stream.Collectors.toCollection(TreeSet::new));
        return new SnippetResponse(
                snippet.getId(),
                snippet.getTitle(),
                snippet.getContent(),
                snippet.getLanguage(),
                snippet.getDescription(),
                tagNames,
                snippet.getCreatedAt(),
                snippet.getUpdatedAt()
        );
    }
}
