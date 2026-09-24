package com.olivier.devhub.snippet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "snippets")
public class Snippet {

    @Id
    private UUID id;

    private String title;

    private String content;

    private String language;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    @ManyToMany
    @JoinTable(
            name = "snippet_tags",
            joinColumns = @JoinColumn(name = "snippet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    protected Snippet() {
    }

    public Snippet(UUID id, String title, String content, String language, String description, Set<Tag> tags) {
        this.id = id;
        update(title, content, language, description, tags);
    }

    public void update(String title, String content, String language, String description, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.language = language;
        this.description = description;
        this.tags.clear();
        this.tags.addAll(tags);
    }

    @PrePersist
    void setCreationDates() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void setUpdateDate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
