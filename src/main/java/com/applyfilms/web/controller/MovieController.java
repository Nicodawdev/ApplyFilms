
package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping
    public List<MovieDTO> getAll() {
        return this.movieService.getAll();
    }

    // GET: obtiene una película por su ID.
    @GetMapping("/{id}")
    public MovieDTO getById(@PathVariable long id) {
        return this.movieService.getById(id);
    }
}

