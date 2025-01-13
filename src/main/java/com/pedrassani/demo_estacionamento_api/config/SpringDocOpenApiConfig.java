package com.pedrassani.demo_estacionamento_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(
                new Info()
                        .title("REST API - Spring Park")
                        .description("Api estacionamento")
                        .version("v1")
                        .contact(new Contact().name("Alexandre").email("alexandrepedrassani@alunos.utfpr.edu.br"))
        );
    }
    private SecurityScheme securityScheme(){
        return new SecurityScheme()
                .description("Insira um beare valido para prosseguir")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
