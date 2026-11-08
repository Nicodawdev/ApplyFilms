package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.SuggestRequestDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
import com.applyfilms.domain.service.ApplyFilmsAiService;
import com.applyfilms.domain.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "Gestión de películas y recomendaciones de ApplyFilms")
public class MovieController {

    private final MovieService movieService;
    private final ApplyFilmsAiService aiService;

    public MovieController(MovieService movieService, ApplyFilmsAiService aiService) {
        this.movieService = movieService;
        this.aiService = aiService;
    }

    // Consultamos todas las películas.
    @GetMapping
    @Operation(summary = "Obtener todas las películas", description = "Devuelve el catálogo de películas", responses = {
            @ApiResponse(responseCode = "200", description = "Catálogo recuperado correctamente")
    })
    public ResponseEntity<List<MovieDTO>> getAll() {
        List<MovieDTO> movies = this.movieService.getAll();
        return ResponseEntity.ok(movies);
    }

    // Consultamos una película por su identificador.
    @GetMapping("/{id}")
    @Operation(summary = "Obtener película por ID", description = "Devuelve la película del identificador indicado", responses = {
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content)
    })
    public ResponseEntity<MovieDTO> getById(@Parameter(description = "Identificador de la película", example = "9") @PathVariable long id) {
        MovieDTO movie = this.movieService.getById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    // Generamos recomendaciones con IA.
    @PostMapping("/suggest")
    @Operation(summary = "Recomendar películas", description = "Solicita hasta tres recomendaciones del catálogo según las preferencias del usuario", responses = {
            @ApiResponse(responseCode = "200", description = "Recomendaciones generadas"),
            @ApiResponse(responseCode = "500", description = "Error al generar recomendaciones", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> generateMoviesSuggestion(@RequestBody SuggestRequestDTO suggestRequestDTO) {
        String suggestion = this.aiService.generateMoviesSuggestion(suggestRequestDTO.userPreferences());
        return ResponseEntity.ok(suggestion);
    }

    // Validamos y creamos una película.
    @PostMapping
    @Operation(summary = "Crear película", description = "Registra una película con datos válidos y título no registrado", responses = {
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, incompletos o título duplicado", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MovieDTO> add(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = this.movieService.add(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    // Actualizamos título, fecha y rating.
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar película", description = "Actualiza el título, la fecha de estreno y el rating de una película existente", responses = {
            @ApiResponse(responseCode = "200", description = "Película actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MovieDTO> update(@Parameter(description = "ID de la película a actualizar", example = "9") @PathVariable long id, @Valid @RequestBody UpdateMovieDTO updateMovieDTO) {
        MovieDTO updatedMovie = this.movieService.update(id, updateMovieDTO);
        return ResponseEntity.ok(updatedMovie);
    }

    // Eliminamos una película.
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar película", description = "Elimina de la base de datos la película indicada", responses = {
            @ApiResponse(responseCode = "204", description = "Película eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la película a eliminar", example = "9") @PathVariable long id) {
        this.movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}