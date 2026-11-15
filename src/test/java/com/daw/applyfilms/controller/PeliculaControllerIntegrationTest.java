package com.daw.applyfilms.controller;

import com.daw.applyfilms.model.Genero;
import com.daw.applyfilms.model.Pelicula;
import com.daw.applyfilms.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PeliculaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PeliculaRepository peliculaRepository;

    private Pelicula guardada;

    private static final String JSON_VALIDO = """
            {"titulo": "Dune", "duracion": 155, "genero": "CIENCIA_FICCION",
             "fechaEstreno": "2021-09-17", "clasificacion": 4.3, "idioma": "en",
             "emailDistribuidora": "info@warnerbros.com", "disponible": true,
             "etiquetas": ["desierto"], "derechosConfirmados": true}
            """;

    @BeforeEach
    void setUp() {
        peliculaRepository.deleteAll();
        guardada = peliculaRepository.save(Pelicula.builder()
                .titulo("Interstellar")
                .duracion(169)
                .genero(Genero.CIENCIA_FICCION)
                .fechaEstreno(LocalDate.of(2014, 11, 7))
                .clasificacion(5.0)
                .idioma("en")
                .emailDistribuidora("info@paramount.com")
                .disponible(false)
                .etiquetas(new ArrayList<>(List.of("espacio")))
                .build());
    }

    @Test
    void getTodas_devuelve200() throws Exception {
        mockMvc.perform(get("/api/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Interstellar"));
    }

    @Test
    void getPorId_existente_devuelve200() throws Exception {
        mockMvc.perform(get("/api/peliculas/" + guardada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Interstellar"))
                .andExpect(jsonPath("$.etiquetas[0]").value("espacio"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/peliculas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void post_valido_devuelve201() throws Exception {
        mockMvc.perform(post("/api/peliculas").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Dune"));
    }

    @Test
    void post_invalido_devuelve400() throws Exception {
        String json = """
                {"titulo": "Futura", "duracion": 100, "genero": "DRAMA", "fechaEstreno": "2999-01-01",
                 "idioma": "es", "emailDistribuidora": "info@test.com", "disponible": true,
                 "derechosConfirmados": true, "clasificacion": 4.55}
                """;

        mockMvc.perform(post("/api/peliculas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erroresCampo.fechaEstreno").value("La fecha de estreno no puede ser futura"))
                .andExpect(jsonPath("$.erroresCampo.clasificacion").exists());
    }

    @Test
    void post_tituloDuplicado_devuelve409() throws Exception {
        String json = JSON_VALIDO.replace("Dune", "Interstellar");

        mockMvc.perform(post("/api/peliculas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void put_existente_devuelve200() throws Exception {
        mockMvc.perform(put("/api/peliculas/" + guardada.getId()).contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(guardada.getId()))
                .andExpect(jsonPath("$.titulo").value("Dune"));
    }

    @Test
    void patch_soloModificaLosCamposEnviados() throws Exception {
        mockMvc.perform(patch("/api/peliculas/" + guardada.getId())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"clasificacion\": 3.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clasificacion").value(3.5))
                .andExpect(jsonPath("$.titulo").value("Interstellar"));
    }

    @Test
    void delete_noDisponible_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/peliculas/" + guardada.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/peliculas/" + guardada.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void mashup_sinConexionConTmdb_devuelve200ConDatosPorDefecto() throws Exception {
        mockMvc.perform(get("/api/peliculas/" + guardada.getId() + "/mashup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Interstellar"))
                .andExpect(jsonPath("$.fuenteExterna").exists());
    }
}
