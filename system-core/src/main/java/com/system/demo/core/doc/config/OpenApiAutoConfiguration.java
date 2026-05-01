package com.system.demo.core.doc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiAutoConfiguration {
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new Info().title("System Demo API").version("1.0.0").description("Generic base framework API"));
    }
}
