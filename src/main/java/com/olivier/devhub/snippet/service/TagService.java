package com.olivier.devhub.snippet.service;

import com.olivier.devhub.snippet.domain.SnippetVisibility;
import com.olivier.devhub.snippet.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public List<String> findByOwnerId(UUID ownerId) {
        return tagRepository.findNamesByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    public List<String> findDemoTags() {
        return tagRepository.findNamesBySnippetVisibility(SnippetVisibility.DEMO);
    }
}
