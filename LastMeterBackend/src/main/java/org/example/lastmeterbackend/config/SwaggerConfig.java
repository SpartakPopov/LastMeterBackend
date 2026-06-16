package org.example.lastmeterbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI lastMeterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LastMeter API")
                        .description("REST API for the LastMeter smart last-mile package delivery platform. " +
                                "Manages packages, order requests, order groups, lockers, notifications, and users.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Sioux Technologies")
                                .url("https://www.sioux.eu")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development server")
                ));
    }
}
