package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.SnippetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/demo/snippets")
public class DemoSnippetController {

    private final SnippetService snippetService;

    public DemoSnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping
    public Collection<SnippetResponse> findAll() {
        return snippetService.findDemoSnippets();
    }

    @GetMapping("/{id}")
    public SnippetResponse findById(@PathVariable UUID id) {
        return snippetService.findDemoSnippet(id);
    }
}
