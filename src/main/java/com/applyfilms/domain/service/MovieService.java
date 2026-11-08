package com.applyfilms.domain.service;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
import com.applyfilms.domain.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Gestionamos las operaciones de películas.
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // Inyectamos el repositorio del dominio.
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Consultamos todas las películas.
    public List<MovieDTO> getAll() {
        return this.movieRepository.getAll();
    }

    // Consultamos una película por su identificador.
    public MovieDTO getById(long id) {
        return this.movieRepository.getById(id);
    }

    // Añadimos una película.
    public MovieDTO add(MovieDTO movieDTO) {
        return this.movieRepository.save(movieDTO);
    }

    // Actualizamos una película.
    public MovieDTO update(long id, UpdateMovieDTO updateMovieDTO) {
        return this.movieRepository.update(id, updateMovieDTO);
    }

    // Solicitamos la eliminación de una película.
    public boolean delete(long id) {
        return this.movieRepository.delete(id);
    }
}
