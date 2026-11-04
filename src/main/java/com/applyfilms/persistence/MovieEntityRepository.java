package com.applyfilms.persistence;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.repository.MovieRepository;
import com.applyfilms.persistence.crud.CrudMovieEntity;
import com.applyfilms.persistence.mapper.MovieMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieEntityRepository implements MovieRepository {
    private final CrudMovieEntity crudMovieEntity;
    private final MovieMapper movieMapper;

    public MovieEntityRepository(CrudMovieEntity crudMovieEntity, MovieMapper movieMapper) {
        this.crudMovieEntity = crudMovieEntity;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDTO> getAll() {
        return this.movieMapper.toDTO(this.crudMovieEntity.findAll());
    }
}
