package com.olivier.devhub.snippet.service;

import com.olivier.devhub.snippet.api.SnippetRequest;
import com.olivier.devhub.snippet.api.SnippetResponse;
import com.olivier.devhub.snippet.domain.Snippet;
import com.olivier.devhub.snippet.domain.SnippetVisibility;
import com.olivier.devhub.snippet.domain.Tag;
import com.olivier.devhub.snippet.repository.SnippetRepository;
import com.olivier.devhub.snippet.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.olivier.devhub.user.domain.UserAccount;
import com.olivier.devhub.user.repository.UserAccountRepository;

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
    private final UserAccountRepository userAccountRepository;

    public SnippetService(SnippetRepository snippetRepository, TagRepository tagRepository, UserAccountRepository userAccountRepository) {
        this.snippetRepository = snippetRepository;
        this.tagRepository = tagRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public Collection<SnippetResponse> findAll(UUID ownerId) {
        return snippetRepository.findAllByOwnerIdOrderByUpdatedAtDesc(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SnippetResponse findById(UUID id, UUID ownerId) {
        return toResponse(findSnippet(id, ownerId));
    }

    @Transactional
    public SnippetResponse create(SnippetRequest request, UUID ownerId) {
        Snippet snippet = new Snippet(
                UUID.randomUUID(),
                findUser(ownerId),
                request.title().trim(),
                request.content(),
                request.language().trim().toLowerCase(Locale.ROOT),
                normalizeDescription(request.description()),
                resolveTags(request.tags())
        );
        return toResponse(snippetRepository.saveAndFlush(snippet));
    }

    @Transactional
    public SnippetResponse update(UUID id, SnippetRequest request, UUID ownerId) {
        Snippet snippet = findSnippet(id, ownerId);
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
    public void delete(UUID id, UUID ownerId) {
        snippetRepository.delete(findSnippet(id, ownerId));
    }

    @Transactional(readOnly = true)
    public Collection<SnippetResponse> findDemoSnippets() {
        return snippetRepository.findAllByVisibilityOrderByUpdatedAtDesc(SnippetVisibility.DEMO).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SnippetResponse findDemoSnippet(UUID id) {
        return toResponse(snippetRepository.findByIdAndVisibility(id, SnippetVisibility.DEMO)
                .orElseThrow(() -> new SnippetNotFoundException(id)));
    }

    private Snippet findSnippet(UUID id, UUID ownerId) {
        return snippetRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new SnippetNotFoundException(id));
    }

    private UserAccount findUser(UUID id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Authenticated user no longer exists."));
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
