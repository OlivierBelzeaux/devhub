package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.SnippetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/snippets")
public class SnippetController {

    private final SnippetService snippetService;

    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping
    public Collection<SnippetResponse> findAll() {
        return snippetService.findAll();
    }

    @GetMapping("/{id}")
    public SnippetResponse findById(@PathVariable UUID id) {
        return snippetService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SnippetResponse> create(@Valid @RequestBody SnippetRequest request) {
        SnippetResponse response = snippetService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public SnippetResponse update(@PathVariable UUID id, @Valid @RequestBody SnippetRequest request) {
        return snippetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        snippetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
