package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.SnippetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/snippets")
@Tag(name = "Snippets", description = "Create and organize code snippets and configurations.")
@SecurityRequirement(name = "bearerAuth")
public class SnippetController {

    private final SnippetService snippetService;

    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping
    @Operation(summary = "List snippets", description = "Filter with query, language or tag. Sort by updatedAt, createdAt, title or language.")
    @ApiResponse(responseCode = "200", description = "Snippets returned")
    public SnippetPageResponse findAll(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return snippetService.search(UUID.fromString(jwt.getSubject()), query, language, tag, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a snippet by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snippet found"),
            @ApiResponse(responseCode = "400", description = "Invalid identifier", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Snippet not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public SnippetResponse findById(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        return snippetService.findById(id, UUID.fromString(jwt.getSubject()));
    }

    @PostMapping
    @Operation(summary = "Create a snippet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Snippet created"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<SnippetResponse> create(@Valid @RequestBody SnippetRequest request, @AuthenticationPrincipal Jwt jwt) {
        SnippetResponse response = snippetService.create(request, UUID.fromString(jwt.getSubject()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a snippet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snippet updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request or identifier", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Snippet not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public SnippetResponse update(@PathVariable UUID id, @Valid @RequestBody SnippetRequest request, @AuthenticationPrincipal Jwt jwt) {
        return snippetService.update(id, request, UUID.fromString(jwt.getSubject()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a snippet")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Snippet deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid identifier", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Snippet not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        snippetService.delete(id, UUID.fromString(jwt.getSubject()));
        return ResponseEntity.noContent().build();
    }
}
