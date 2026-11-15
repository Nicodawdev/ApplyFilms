package com.daw.applyfilms.config;

import com.daw.applyfilms.model.Genero;
import com.daw.applyfilms.model.Pelicula;
import com.daw.applyfilms.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Precarga de datos de prueba. Solo se ejecuta con el perfil "dev".
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final PeliculaRepository peliculaRepository;

    @Override
    public void run(String... args) {
        List<Pelicula> peliculas = List.of(
                crear("Shrek", 90, Genero.ANIMADA, "2001-05-18", 4.2, "en", "info@dreamworks.com", true, List.of("ogro", "humor")),
                crear("Inception", 148, Genero.CIENCIA_FICCION, "2010-07-16", 4.7, "en", "contacto@warnerbros.com", true, List.of("sueños", "nolan")),
                crear("Titanic", 195, Genero.DRAMA, "1997-12-19", 4.6, "en", "info@paramount.com", true, List.of("romance", "barco")),
                crear("John Wick", 101, Genero.ACCION, "2014-10-24", null, "en", "info@lionsgate.com", false, List.of("venganza")),
                crear("El Conjuro", 112, Genero.TERROR, "2013-07-19", 3.0, "en", "contacto@warnerbros.com", true, List.of("paranormal")),
                crear("Coco", 105, Genero.ANIMADA, "2017-10-27", 4.7, "en", "info@pixar.com", true, List.of("musica", "familia")),
                crear("Interstellar", 169, Genero.CIENCIA_FICCION, "2014-11-07", 5.0, "en", "info@paramount.com", true, List.of("espacio", "nolan")),
                crear("Joker", 122, Genero.DRAMA, "2019-10-04", null, "en", "contacto@warnerbros.com", false, List.of("dc")),
                crear("El laberinto del fauno", 118, Genero.DRAMA, "2006-10-11", 4.4, "es", "info@warnerbros.es", true, List.of("fantasia", "guerra")),
                crear("Ocho apellidos vascos", 98, Genero.COMEDIA, "2014-03-14", 3.5, "es", "info@universal.es", false, List.of("humor", "españa"))
        );

        peliculaRepository.saveAll(peliculas);
        log.info("Precarga DEV completada: {} películas insertadas", peliculas.size());
    }

    private Pelicula crear(String titulo, Integer duracion, Genero genero, String fecha, Double clasificacion,
                           String idioma, String email, Boolean disponible, List<String> etiquetas) {
        return Pelicula.builder()
                .titulo(titulo)
                .duracion(duracion)
                .genero(genero)
                .fechaEstreno(LocalDate.parse(fecha))
                .clasificacion(clasificacion)
                .idioma(idioma)
                .emailDistribuidora(email)
                .disponible(disponible)
                .etiquetas(new ArrayList<>(etiquetas))
                .build();
    }
}
