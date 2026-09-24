package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Snippet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnippetRepository extends JpaRepository<Snippet, UUID> {

    @EntityGraph(attributePaths = "tags")
    List<Snippet> findAllByOrderByUpdatedAtDesc();

    @Override
    @EntityGraph(attributePaths = "tags")
    Optional<Snippet> findById(UUID id);
}
