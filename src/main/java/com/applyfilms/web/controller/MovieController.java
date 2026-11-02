package com.applyfilms.web.controller;

import com.applyfilms.persistence.crud.CrudMovieEntity;
import com.applyfilms.persistence.entity.MovieEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    private final CrudMovieEntity CrudMovieEntity;

    public MovieController (CrudMovieEntity crudMovieEntity) {
        this.CrudMovieEntity=crudMovieEntity;
    }

    @GetMapping("/movies")
    public List <MovieEntity> getAll() {
        return (List<MovieEntity>)  this.CrudMovieEntity.findAll();
    }
}
