package com.bolt.headless_auth_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bolt Headless Auth API - v1.0",
                version = "1.0",
                description = "A headless auth API to generate and validate OTPs without touching your core database."
        ),
        servers = {
                @Server(url = "http://localhost:8080/", description = "Local Development")


        }
)

public class SwaggerUIConfiguration {
}
