package com.daw.applyfilms.service;

import com.daw.applyfilms.dto.PeliculaCreacionDTO;
import com.daw.applyfilms.dto.PeliculaMashupDTO;
import com.daw.applyfilms.dto.PeliculaPatchDTO;
import com.daw.applyfilms.dto.PeliculaResumenDTO;
import com.daw.applyfilms.exception.RecursoNoEncontradoException;
import com.daw.applyfilms.exception.ReglaNegocioException;
import com.daw.applyfilms.model.Genero;
import com.daw.applyfilms.model.Pelicula;
import com.daw.applyfilms.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceUnitTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @Mock
    private ExternalApiService externalApiService;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula pelicula;

    @BeforeEach
    void setUp() {
        pelicula = Pelicula.builder()
                .id(1L)
                .titulo("Interstellar")
                .duracion(169)
                .genero(Genero.CIENCIA_FICCION)
                .fechaEstreno(LocalDate.of(2014, 11, 7))
                .clasificacion(5.0)
                .idioma("en")
                .emailDistribuidora("info@paramount.com")
                .disponible(true)
                .etiquetas(new ArrayList<>(List.of("espacio")))
                .build();
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveElDTO() {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        PeliculaResumenDTO resultado = peliculaService.obtenerPorId(1L);

        assertThat(resultado.titulo()).isEqualTo("Interstellar");
        assertThat(resultado.etiquetas()).containsExactly("espacio");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaRecursoNoEncontrado() {
        when(peliculaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> peliculaService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_guardaLaPeliculaYDevuelveElDTO() {
        PeliculaCreacionDTO dto = new PeliculaCreacionDTO("Coco", 105, Genero.ANIMADA, LocalDate.of(2017, 10, 27),
                4.7, "en", "info@pixar.com", true, List.of("musica"), true);
        when(peliculaRepository.saveAndFlush(any(Pelicula.class))).thenAnswer(inv -> {
            Pelicula p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        PeliculaResumenDTO resultado = peliculaService.crear(dto);

        assertThat(resultado.id()).isEqualTo(10L);
        assertThat(resultado.titulo()).isEqualTo("Coco");
        verify(peliculaRepository).saveAndFlush(any(Pelicula.class));
    }

    @Test
    void actualizarParcial_soloCambiaLosCamposNoNulos() {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(peliculaRepository.saveAndFlush(pelicula)).thenReturn(pelicula);
        PeliculaPatchDTO dto = new PeliculaPatchDTO(null, null, null, null, 4.0, null, null, null, null);

        PeliculaResumenDTO resultado = peliculaService.actualizarParcial(1L, dto);

        assertThat(resultado.clasificacion()).isEqualTo(4.0);
        assertThat(resultado.titulo()).isEqualTo("Interstellar");
        assertThat(resultado.duracion()).isEqualTo(169);
    }

    @Test
    void eliminar_peliculaDisponible_lanzaReglaNegocio() {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        assertThatThrownBy(() -> peliculaService.eliminar(1L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("disponible");
        verify(peliculaRepository, never()).delete(any());
    }

    @Test
    void eliminar_peliculaNoDisponible_laBorra() {
        pelicula.setDisponible(false);
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        peliculaService.eliminar(1L);

        verify(peliculaRepository).delete(pelicula);
    }

    @Test
    void obtenerMashup_siTmdbFalla_devuelveDatosPorDefecto() {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(externalApiService.buscarPelicula("Interstellar", 2014)).thenReturn(null);

        PeliculaMashupDTO resultado = peliculaService.obtenerMashup(1L);

        assertThat(resultado.titulo()).isEqualTo("Interstellar");
        assertThat(resultado.fuenteExterna()).isEqualTo("NO DISPONIBLE");
        assertThat(resultado.sinopsis()).isEqualTo("No disponible");
    }
}
