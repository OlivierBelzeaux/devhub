package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tags", description = "List tags used by the authenticated user.")
@SecurityRequirement(name = "bearerAuth")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "List my tags")
    @ApiResponse(responseCode = "200", description = "Tags returned alphabetically")
    public List<String> findAll(@AuthenticationPrincipal Jwt jwt) {
        return tagService.findByOwnerId(UUID.fromString(jwt.getSubject()));
    }
}
