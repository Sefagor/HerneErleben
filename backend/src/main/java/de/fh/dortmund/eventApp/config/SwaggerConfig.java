package de.fh.dortmund.eventApp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi
                .builder()
                .group("public-apis")
                .group("private-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("App Management API").version("1.O"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")));
    }
}