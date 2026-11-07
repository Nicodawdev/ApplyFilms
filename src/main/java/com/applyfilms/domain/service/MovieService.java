package com.applyfilms.domain.service;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDTO> getAll() {
        return this.movieRepository.getAll();
    }

    public MovieDTO getById(@PathVariable long id) {
        return this.movieRepository.getById(id);
    }

    public MovieDTO add(MovieDTO movieDTO) {
        return this.movieRepository.save(movieDTO);
    }
}
