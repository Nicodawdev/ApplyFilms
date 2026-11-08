package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
import com.applyfilms.domain.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

// Gestionamos las peticiones HTTP de películas.
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    // Inyectamos el servicio.
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Consultamos todas las películas.
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAll() {
        List<MovieDTO> movies = this.movieService.getAll();

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

    // Creamos una película.
    @PostMapping
    public ResponseEntity<MovieDTO> add(@RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = this.movieService.add(movieDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMovie);
    }

    // Actualizamos una película existente.
    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> update(
            @PathVariable long id,
            @RequestBody UpdateMovieDTO updateMovieDTO
    ) {
        MovieDTO updatedMovie = this.movieService.update(id, updateMovieDTO);

        if (updatedMovie == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMovie);
    }

    // Eliminamos una película por su identificador.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = this.movieService.delete(id);

        // Si no existe, devolvemos 404.
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        // La eliminación se ha realizado correctamente.
        return ResponseEntity.noContent().build();
    }
}

