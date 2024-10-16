package com.pdev.stocktracker.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class OpenAPIConfig {

    @Bean
    OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("renanlessa@gmail.com");
        contact.setName("Renan Lessa");
        contact.setUrl("https://www.youtube.com/@paneladev");

        Info info = new Info()
                .title("Stock Tracking Profile API")
                .version("v1")
                .contact(contact)
                .description("API para gerenciamento de compra de ações na bolsa de valores.");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Server URL local environment");

        Server devServer = new Server();
        devServer.setUrl("https://dev.com");
        devServer.setDescription("Server URL DEV environment");

        return new OpenAPI().info(info).servers(List.of(localServer, devServer));
    }
}
