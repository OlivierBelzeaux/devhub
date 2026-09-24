package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Snippet;
import com.olivier.devhub.snippet.domain.SnippetVisibility;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnippetRepository extends JpaRepository<Snippet, UUID> {

    @EntityGraph(attributePaths = "tags")
    List<Snippet> findAllByOwnerIdOrderByUpdatedAtDesc(UUID ownerId);

    @EntityGraph(attributePaths = "tags")
    Optional<Snippet> findByIdAndOwnerId(UUID id, UUID ownerId);

    @EntityGraph(attributePaths = "tags")
    List<Snippet> findAllByVisibilityOrderByUpdatedAtDesc(SnippetVisibility visibility);

    @EntityGraph(attributePaths = "tags")
    Optional<Snippet> findByIdAndVisibility(UUID id, SnippetVisibility visibility);
}
