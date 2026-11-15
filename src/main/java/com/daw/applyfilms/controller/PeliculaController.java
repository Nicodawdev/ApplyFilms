package com.daw.applyfilms.controller;

import com.daw.applyfilms.dto.ErrorResponse;
import com.daw.applyfilms.dto.PeliculaCreacionDTO;
import com.daw.applyfilms.dto.PeliculaMashupDTO;
import com.daw.applyfilms.dto.PeliculaPatchDTO;
import com.daw.applyfilms.dto.PeliculaResumenDTO;
import com.daw.applyfilms.dto.RecomendacionRequestDTO;
import com.daw.applyfilms.service.ApplyFilmsAiService;
import com.daw.applyfilms.service.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/peliculas")
@RequiredArgsConstructor
@Tag(name = "Películas", description = "CRUD de películas, mashup con TMDB y recomendaciones con IA")
public class PeliculaController {

    private final PeliculaService peliculaService;
    private final ApplyFilmsAiService aiService;
    private final CacheManager cacheManager;

    @GetMapping
    @Operation(summary = "Listar películas", description = "Devuelve todas las películas del catálogo")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<PeliculaResumenDTO>> obtenerTodas() {
        return ResponseEntity.ok(peliculaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener película por ID", description = "Devuelve la película con el identificador indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "404", description = "La película no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeliculaResumenDTO> obtenerPorId(
            @Parameter(description = "ID de la película", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar por título", description = "Busca películas cuyo título contenga el texto indicado (sin distinguir mayúsculas)")
    @ApiResponse(responseCode = "200", description = "Resultados de la búsqueda")
    public ResponseEntity<List<PeliculaResumenDTO>> buscarPorTitulo(
            @Parameter(description = "Texto a buscar", example = "star") @RequestParam String titulo) {
        return ResponseEntity.ok(peliculaService.buscarPorTitulo(titulo));
    }

    @GetMapping("/valoradas")
    @Operation(summary = "Buscar por valoración mínima", description = "Devuelve las películas con una clasificación mayor o igual a la indicada")
    @ApiResponse(responseCode = "200", description = "Resultados de la búsqueda")
    public ResponseEntity<List<PeliculaResumenDTO>> buscarPorClasificacion(
            @Parameter(description = "Clasificación mínima (0-5)", example = "4.5") @RequestParam Double minimo) {
        return ResponseEntity.ok(peliculaService.buscarPorClasificacionMinima(minimo));
    }

    @PostMapping
    @Operation(summary = "Crear película", description = "Registra una nueva película validando sus datos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe una película con ese título",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeliculaResumenDTO> crear(@Valid @RequestBody PeliculaCreacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(peliculaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplazar película", description = "Sustituye todos los datos de una película existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película reemplazada"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "La película no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe otra película con ese título",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeliculaResumenDTO> reemplazar(
            @Parameter(description = "ID de la película", example = "1") @PathVariable Long id,
            @Valid @RequestBody PeliculaCreacionDTO dto) {
        return ResponseEntity.ok(peliculaService.reemplazar(id, dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modificar parcialmente", description = "Modifica solo los campos enviados (los nulos se ignoran)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película modificada"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "La película no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe otra película con ese título",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeliculaResumenDTO> actualizarParcial(
            @Parameter(description = "ID de la película", example = "1") @PathVariable Long id,
            @Valid @RequestBody PeliculaPatchDTO dto) {
        return ResponseEntity.ok(peliculaService.actualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar película", description = "Borra la película. Solo se permite si no está disponible en el catálogo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Película eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "La película no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "No se puede borrar una película disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la película", example = "4") @PathVariable Long id) {
        peliculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/mashup")
    @Operation(summary = "Mashup con TMDB", description = "Une los datos locales con los de The Movie Database. La respuesta se guarda en caché")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos combinados (si TMDB falla, los campos externos vienen por defecto)"),
            @ApiResponse(responseCode = "404", description = "La película no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeliculaMashupDTO> obtenerMashup(
            @Parameter(description = "ID de la película", example = "7") @PathVariable Long id) {
        Cache cache = cacheManager.getCache("mashup");
        boolean estabaEnCache = cache != null && cache.get(id) != null;

        long inicio = System.currentTimeMillis();
        PeliculaMashupDTO mashup = peliculaService.obtenerMashup(id);
        long tiempo = System.currentTimeMillis() - inicio;

        log.info("[MASHUP] id={} servido desde {} en {} ms",
                id, estabaEnCache ? "CACHÉ RAM" : "BD H2 + TMDB", tiempo);
        return ResponseEntity.ok(mashup);
    }

    @PostMapping("/recomendar")
    @Operation(summary = "Recomendar películas con IA", description = "La IA recomienda hasta 3 películas del catálogo según los gustos del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recomendaciones generadas"),
            @ApiResponse(responseCode = "400", description = "Preferencias vacías",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al contactar con la IA",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> recomendar(@Valid @RequestBody RecomendacionRequestDTO dto) {
        return ResponseEntity.ok(aiService.recomendarPeliculas(dto.preferencias()));
    }
}
