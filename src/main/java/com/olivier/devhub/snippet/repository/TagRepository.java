package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Tag;
import com.olivier.devhub.snippet.domain.SnippetVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    List<Tag> findByNameIn(Collection<String> names);

    @Query("""
            select distinct tag.name
            from Snippet snippet join snippet.tags tag
            where snippet.owner.id = :ownerId
            order by tag.name
            """)
    List<String> findNamesByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("""
            select distinct tag.name
            from Snippet snippet join snippet.tags tag
            where snippet.visibility = :visibility
            order by tag.name
            """)
    List<String> findNamesBySnippetVisibility(@Param("visibility") SnippetVisibility visibility);
}
