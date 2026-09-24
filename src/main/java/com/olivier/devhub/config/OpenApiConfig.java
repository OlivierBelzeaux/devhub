package com.olivier.devhub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI devHubOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevHub API")
                        .version("v1")
                        .description("API for storing and organizing development snippets."));
    }
}
