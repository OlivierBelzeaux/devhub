package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Snippet;
import com.olivier.devhub.snippet.domain.SnippetVisibility;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnippetRepository extends JpaRepository<Snippet, UUID>, JpaSpecificationExecutor<Snippet> {

    @Override
    @EntityGraph(attributePaths = "tags")
    Page<Snippet> findAll(Specification<Snippet> specification, Pageable pageable);

    @EntityGraph(attributePaths = "tags")
    List<Snippet> findAllByOwnerIdOrderByUpdatedAtDesc(UUID ownerId);

    @EntityGraph(attributePaths = "tags")
    Optional<Snippet> findByIdAndOwnerId(UUID id, UUID ownerId);

    @EntityGraph(attributePaths = "tags")
    List<Snippet> findAllByVisibilityOrderByUpdatedAtDesc(SnippetVisibility visibility);

    @EntityGraph(attributePaths = "tags")
    Optional<Snippet> findByIdAndVisibility(UUID id, SnippetVisibility visibility);
}
