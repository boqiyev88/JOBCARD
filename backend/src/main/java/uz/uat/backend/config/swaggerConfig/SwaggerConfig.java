package uz.uat.backend.config.swaggerConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        final String authSchemaName = "BearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().
                        addList(authSchemaName))
                .components(new Components()
                        .addSecuritySchemes(
                                authSchemaName,
                                new SecurityScheme()
                                        .name(authSchemaName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}