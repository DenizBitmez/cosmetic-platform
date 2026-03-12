package com.cosmeticPlatform.CosmeticPlatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cosmeticPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cosmetic Platform API")
                        .description("API documentation for the Cosmetic Platform application, including product management, user routines, and sustainability metrics.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Cosmetic Platform Support")
                                .email("support@cosmeticplatform.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
