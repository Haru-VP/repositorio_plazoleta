package com.pragma.powerup.infrastructure.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "jwtAuth";

    @Bean
    public OpenAPI customOpenApi(@Value("${appdescription}") String appDescription,
                                 @Value("${appversion}") String appVersion) {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("Microservicio de Plazoleta - PowerUp Plazoleta")
                        .version(appVersion)
                        .description("API REST para la gestión de restaurantes, menús, platos y ciclo de vida de los pedidos en la plataforma de Plazoleta de Comidas bajo Arquitectura Hexagonal.")
                        .contact(new Contact()
                                .name("Valentina Pinto")
                                .email("valentina.pinto@pragma.com.co"))
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}