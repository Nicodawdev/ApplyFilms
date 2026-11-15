package com.daw.applyfilms.repository;

import com.daw.applyfilms.model.Genero;
import com.daw.applyfilms.model.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ImportAutoConfiguration(CacheAutoConfiguration.class)
class PeliculaRepositoryTest {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @BeforeEach
    void setUp() {
        peliculaRepository.save(crear("Star Wars", 4.5));
        peliculaRepository.save(crear("Star Trek", 3.8));
        peliculaRepository.save(crear("Titanic", 4.6));
    }

    @Test
    void findByTituloContainingIgnoreCase_devuelveLasQueContienenElTexto() {
        List<Pelicula> resultado = peliculaRepository.findByTituloContainingIgnoreCase("STAR");

        assertThat(resultado).hasSize(2)
                .extracting(Pelicula::getTitulo)
                .containsExactlyInAnyOrder("Star Wars", "Star Trek");
    }

    @Test
    void findByClasificacionGreaterThanEqual_filtraPorValoracionMinima() {
        List<Pelicula> resultado = peliculaRepository.findByClasificacionGreaterThanEqual(4.5);

        assertThat(resultado).extracting(Pelicula::getTitulo)
                .containsExactlyInAnyOrder("Star Wars", "Titanic");
    }

    @Test
    void save_guardaLasEtiquetasDeLaPelicula() {
        Pelicula guardada = peliculaRepository.findByTitulo("Titanic").orElseThrow();

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getEtiquetas()).containsExactly("test");
    }

    private Pelicula crear(String titulo, Double clasificacion) {
        return Pelicula.builder()
                .titulo(titulo)
                .duracion(120)
                .genero(Genero.CIENCIA_FICCION)
                .fechaEstreno(LocalDate.of(2000, 1, 1))
                .clasificacion(clasificacion)
                .idioma("en")
                .emailDistribuidora("info@test.com")
                .disponible(true)
                .etiquetas(new ArrayList<>(List.of("test")))
                .build();
    }
}
