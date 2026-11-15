package com.daw.applyfilms.controller;

import com.daw.applyfilms.dto.PeliculaCreacionDTO;
import com.daw.applyfilms.dto.PeliculaResumenDTO;
import com.daw.applyfilms.exception.RecursoNoEncontradoException;
import com.daw.applyfilms.exception.ReglaNegocioException;
import com.daw.applyfilms.model.Genero;
import com.daw.applyfilms.service.ApplyFilmsAiService;
import com.daw.applyfilms.service.PeliculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeliculaController.class)
@ActiveProfiles("test")
@ImportAutoConfiguration(CacheAutoConfiguration.class)
class PeliculaControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PeliculaService peliculaService;

    @MockitoBean
    private ApplyFilmsAiService aiService;

    private final PeliculaResumenDTO interstellar = new PeliculaResumenDTO(1L, "Interstellar", 169,
            Genero.CIENCIA_FICCION, LocalDate.of(2014, 11, 7), 5.0, "en", "info@paramount.com", true, List.of("espacio"));

    @Test
    void obtenerTodas_devuelve200ConLaLista() throws Exception {
        when(peliculaService.obtenerTodas()).thenReturn(List.of(interstellar));

        mockMvc.perform(get("/api/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Interstellar"));

        verify(peliculaService).obtenerTodas();
    }

    @Test
    void obtenerPorId_inexistente_devuelve404() throws Exception {
        when(peliculaService.obtenerPorId(99L)).thenThrow(new RecursoNoEncontradoException(99L));

        mockMvc.perform(get("/api/peliculas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("La película con ID 99 no existe"));
    }

    @Test
    void crear_conDatosValidos_devuelve201() throws Exception {
        when(peliculaService.crear(any(PeliculaCreacionDTO.class))).thenReturn(interstellar);

        String json = """
                {"titulo": "Interstellar", "duracion": 169, "genero": "CIENCIA_FICCION",
                 "fechaEstreno": "2014-11-07", "clasificacion": 5.0, "idioma": "en",
                 "emailDistribuidora": "info@paramount.com", "disponible": true,
                 "etiquetas": ["espacio"], "derechosConfirmados": true}
                """;

        mockMvc.perform(post("/api/peliculas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(peliculaService).crear(any(PeliculaCreacionDTO.class));
    }

    @Test
    void crear_conDatosInvalidos_devuelve400SinLlamarAlServicio() throws Exception {
        String json = """
                {"titulo": "", "duracion": -5, "genero": "DRAMA", "fechaEstreno": "2014-11-07",
                 "idioma": "ESP", "emailDistribuidora": "no-es-un-email", "disponible": true,
                 "derechosConfirmados": false}
                """;

        mockMvc.perform(post("/api/peliculas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erroresCampo.titulo").exists())
                .andExpect(jsonPath("$.erroresCampo.duracion").exists())
                .andExpect(jsonPath("$.erroresCampo.idioma").exists())
                .andExpect(jsonPath("$.erroresCampo.emailDistribuidora").exists())
                .andExpect(jsonPath("$.erroresCampo.derechosConfirmados").exists());

        verify(peliculaService, never()).crear(any());
    }

    @Test
    void eliminar_cuandoElServicioNoFalla_devuelve204SinCuerpo() throws Exception {
        // El servicio es un mock: no hay BD. Simulamos que el borrado va bien (no lanza excepción).
        doNothing().when(peliculaService).eliminar(4L);

        mockMvc.perform(delete("/api/peliculas/4"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(peliculaService).eliminar(4L);
    }

    @Test
    void eliminar_peliculaDisponible_devuelve422() throws Exception {
        doThrow(new ReglaNegocioException("No se puede eliminar")).when(peliculaService).eliminar(1L);

        mockMvc.perform(delete("/api/peliculas/1"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value(422));
    }
}
