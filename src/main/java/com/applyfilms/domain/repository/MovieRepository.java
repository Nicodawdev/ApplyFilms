package com.applyfilms.domain.repository;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;

import java.util.List;

// Definimos las operaciones del repositorio de películas.
public interface MovieRepository {

    List<MovieDTO> getAll();

    MovieDTO getById(long id);

    MovieDTO save(MovieDTO movieDTO);

    MovieDTO update(long id, UpdateMovieDTO updateMovieDTO);

    boolean delete(long id);
}
