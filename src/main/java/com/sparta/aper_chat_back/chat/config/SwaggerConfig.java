package com.sparta.aper_chat_back.chat.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {
    @Bean
    @Profile("!Prod")
    public OpenAPI openAPI() {
        String jwtSchemeName = "Bearer 토큰 입력";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("Bearer"));

        Server httpsServer = new Server()
                .url("https://chat.aper.cc")
                .description("HTTPS 서버");

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(apiInfo())
                .addServersItem(httpsServer);
    }

    private Info apiInfo() {
        return new Info()
                .title("Chat Service API")
                .description("This API provides endpoints for managing chat")
                .version("1.0.0");
    }
}

