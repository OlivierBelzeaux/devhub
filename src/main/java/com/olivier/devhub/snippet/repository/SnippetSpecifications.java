package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Snippet;
import com.olivier.devhub.snippet.domain.SnippetVisibility;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.UUID;

public final class SnippetSpecifications {

    private SnippetSpecifications() {
    }

    public static Specification<Snippet> ownedBy(UUID ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Snippet> visibleAsDemo() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("visibility"), SnippetVisibility.DEMO);
    }

    public static Specification<Snippet> matchesQuery(String value) {
        String pattern = "%" + escapeLike(value.toLowerCase(Locale.ROOT)) + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern, '\\'),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), pattern, '\\'),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern, '\\')
        );
    }

    public static Specification<Snippet> hasLanguage(String language) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("language")), language.toLowerCase(Locale.ROOT));
    }

    public static Specification<Snippet> hasTag(String tag) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.join("tags").get("name")), tag.toLowerCase(Locale.ROOT));
    }

    private static String escapeLike(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
