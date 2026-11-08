package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.SuggestRequestDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
import com.applyfilms.domain.service.ApplyFilmsAiService;
import com.applyfilms.domain.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Gestionamos las peticiones HTTP de películas.
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final ApplyFilmsAiService aiService;

    // Inyectamos el servicio de películas y el servicio de IA.
    public MovieController(MovieService movieService, ApplyFilmsAiService aiService) {
        this.movieService = movieService;
        this.aiService = aiService;
    }

    // Consultamos todas las películas.
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAll() {List<MovieDTO> movies = this.movieService.getAll();

        return ResponseEntity.ok(movies);
    }

    // Consultamos una película por su identificador.
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable long id) {
        MovieDTO movie = this.movieService.getById(id);

        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movie);
    }

    // Generamos recomendaciones según las preferencias del usuario.
    @PostMapping("/suggest")
    public ResponseEntity<String> generateMoviesSuggestion(@RequestBody SuggestRequestDTO suggestRequestDTO) {
        String suggestion = this.aiService.generateMoviesSuggestion(
                suggestRequestDTO.userPreferences()
        );

        return ResponseEntity.ok(suggestion);
    }

    // Validamos los datos antes de crear la película.
    @PostMapping
    public ResponseEntity<MovieDTO> add(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = this.movieService.add(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    // Actualizamos una película.
    // Si no existe, la excepción se gestiona en RestExceptionHandler.
    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> update(@PathVariable long id, @RequestBody @Valid UpdateMovieDTO updateMovieDTO) {
        MovieDTO updatedMovie = this.movieService.update(id, updateMovieDTO);

        return ResponseEntity.ok(updatedMovie);
    }

    // Eliminamos una película.
    // Si no existe, la excepción se gestiona en RestExceptionHandler.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        this.movieService.delete(id);

        return ResponseEntity.noContent().build();
    }
}