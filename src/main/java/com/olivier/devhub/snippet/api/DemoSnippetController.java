package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.SnippetService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/demo/snippets")
public class DemoSnippetController {

    private final SnippetService snippetService;

    public DemoSnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping
    public SnippetPageResponse findAll(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return snippetService.searchDemoSnippets(query, language, tag, pageable);
    }

    @GetMapping("/{id}")
    public SnippetResponse findById(@PathVariable UUID id) {
        return snippetService.findDemoSnippet(id);
    }
}
