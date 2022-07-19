package com.techaccelarators.ifind.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String SCHEME_NAME = "bearerScheme";
    private static final String SCHEME = "Bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI()
                .info(getInfo());

        addSecurity(openApi);

        return openApi;
    }

    private Info getInfo() {
        return new Info()
                .title("iFind APIs Documentation")
                .description("The API documentation for iFind application.")
                .version("1.0.0")
                .license(getLicense());
    }

    private License getLicense() {
        return new License()
                .name("Tafadzwa Allen Pundo")
                .url("https://www.yours.com/en/");
    }
    private void addSecurity(OpenAPI openApi) {
        Components components = createComponents();
        SecurityRequirement securityItem = new SecurityRequirement().addList(SCHEME_NAME);

        openApi
                .components(components)
                .addSecurityItem(securityItem);
    }

    private Components createComponents() {
        Components components = new Components();
        components.addSecuritySchemes(SCHEME_NAME, createSecurityScheme());

        return components;
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }
}
