package com.applyfilms.persistence;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.repository.MovieRepository;
import com.applyfilms.persistence.crud.CrudMovieEntity;
import com.applyfilms.persistence.entity.MovieEntity;
import com.applyfilms.persistence.mapper.MovieMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// Implementamos el repositorio del dominio utilizando JPA.
@Repository
public class MovieEntityRepository implements MovieRepository {

    private final CrudMovieEntity crudMovieEntity;
    private final MovieMapper movieMapper;

    // Inyectamos el repositorio JPA y el mapper.
    public MovieEntityRepository(
            CrudMovieEntity crudMovieEntity,
            MovieMapper movieMapper
    ) {
        this.crudMovieEntity = crudMovieEntity;
        this.movieMapper = movieMapper;
    }

    // Consultamos todas las entidades y las convertimos a DTO.
    @Override
    public List<MovieDTO> getAll() {
        return this.movieMapper.toDTO(this.crudMovieEntity.findAll());
    }

    // Buscamos una película por su identificador.
    @Override
    public MovieDTO getById(long id) {
        MovieEntity movieEntity = this.crudMovieEntity
                .findById(id)
                .orElse(null);

        return this.movieMapper.toDTO(movieEntity);
    }

    // Convertimos el DTO en entidad y guardamos la película.
    @Override
    public MovieDTO save(MovieDTO movieDTO) {

        // Creamos la entidad a partir del DTO recibido.
        MovieEntity movieEntity = this.movieMapper.toEntity(movieDTO);

        // Asignamos el estado a ESTA película.
        movieEntity.setEstado("0");

        // Guardamos la entidad en la base de datos.
        MovieEntity savedMovie = this.crudMovieEntity.save(movieEntity);

        // Devolvemos los datos guardados como DTO.
        return this.movieMapper.toDTO(savedMovie);
    }
}