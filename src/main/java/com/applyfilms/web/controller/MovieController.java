package com.applyfilms.web.controller;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.service.MovieService;
import com.applyfilms.persistence.entity.MovieEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController (MovieService movieService) {
        this.movieService = movieService;

    }

    @GetMapping("/movies")
    public List <MovieDTO> getAll() {
        return this.movieService.getAll();
    }
}
