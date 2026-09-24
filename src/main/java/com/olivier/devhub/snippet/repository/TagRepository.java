package com.olivier.devhub.snippet.repository;

import com.olivier.devhub.snippet.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    List<Tag> findByNameIn(Collection<String> names);
}
