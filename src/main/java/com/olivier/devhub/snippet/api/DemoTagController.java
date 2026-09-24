package com.olivier.devhub.snippet.api;

import com.olivier.devhub.snippet.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/demo/tags")
@Tag(name = "Demo tags", description = "List tags available in the public demo.")
public class DemoTagController {

    private final TagService tagService;

    public DemoTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "List demo tags")
    @ApiResponse(responseCode = "200", description = "Tags returned alphabetically")
    public List<String> findAll() {
        return tagService.findDemoTags();
    }
}
