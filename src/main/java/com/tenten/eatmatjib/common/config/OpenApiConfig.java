package com.tenten.eatmatjib.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String ACCESS_TOKEN_KEY = "Access Token";

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        Components components = new Components()
                .addSecuritySchemes(ACCESS_TOKEN_KEY, accessTokenSecurityScheme);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(ACCESS_TOKEN_KEY);

        return new OpenAPI()
                .info(new Info().title("EatMatJib Server API Docs"))
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}

