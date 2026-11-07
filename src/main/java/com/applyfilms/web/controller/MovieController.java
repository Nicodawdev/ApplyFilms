package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que gestiona las peticiones HTTP de películas.
@RestController
@RequestMapping("/movies")
public class MovieController {

    // Servicio que contiene la lógica de negocio.
    private final MovieService movieService;

    // Constructor para inyectar el servicio.
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET: obtiene todas las películas.
    // Devuelve un código HTTP 200 (OK) con la lista.
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAll() {

        List<MovieDTO> movies = movieService.getAll();

        return ResponseEntity.ok(movies);
    }

    // GET: obtiene una película por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable long id) {

        MovieDTO movie = movieService.getById(id);

        // Si no encontramos la película, devolvemos 404.
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        // Si existe, devolvemos 200 OK con sus datos.
        return ResponseEntity.ok(movie);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> add(@RequestBody MovieDTO movieDTO) {
        MovieDTO movieDTOResponse = this.movieService.add(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieDTOResponse);
    }
}


