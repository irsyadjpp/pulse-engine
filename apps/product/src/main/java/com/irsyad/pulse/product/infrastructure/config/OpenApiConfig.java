package com.irsyad.pulse.product.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI documentation configuration (springdoc-openapi).
 * Documents OAuth2 security scheme with product.read, product.write,
 * product.publish and audit.read scopes (FSD_06 Section 12).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productCatalogOpenApi() {
        String securitySchemeName = "oauth2";
        return new OpenAPI()
                .info(new Info()
                        .title("Pulse Engine - Product Catalog Service API")
                        .version("1.0")
                        .description("Single Source of Truth for Personal Accident Insurance product metadata."))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .clientCredentials(new OAuthFlow()
                                                .scopes(new Scopes()
                                                        .addString("product.read", "Read product metadata")
                                                        .addString("product.write", "Write product metadata")
                                                        .addString("product.publish", "Publish or archive product")
                                                        .addString("audit.read", "Read audit trail"))))));
    }
}
