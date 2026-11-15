package com.daw.applyfilms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(
        title = "ApplyFilms API",
        version = "1.0",
        description = "API RESTful de gestión de películas con mashup de The Movie Database (TMDB), caché RAM e IA"))
public class ApplyFilmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplyFilmsApplication.class, args);
    }
}
