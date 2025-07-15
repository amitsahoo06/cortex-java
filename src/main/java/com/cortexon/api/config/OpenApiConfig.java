package com.cortexon.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) configuration for the application
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CortexON API")
                        .description("CortexON API for orchestrating AI agents")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CortexON Team")
                                .url("https://github.com/TheAgenticAI/CortexON")
                                .email("info@cortexon.ai"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:12000" + contextPath)
                                .description("Local server")
                ));
    }
}