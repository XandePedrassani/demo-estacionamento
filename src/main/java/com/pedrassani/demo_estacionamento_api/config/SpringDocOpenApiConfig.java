package com.pedrassani.demo_estacionamento_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("REST API - Spring Park")
                        .description("Api estacionamento")
                        .version("v1")
                        .contact(new Contact().name("Alexandre").email("alexandrepedrassani@alunos.utfpr.edu.br"))
        );
    }
}
