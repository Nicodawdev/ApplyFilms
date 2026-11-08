package com.applyfilms.persistence;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
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

    // Consultamos todas las películas.
    @Override
    public List<MovieDTO> getAll() {
        return this.movieMapper.toDTO(this.crudMovieEntity.findAll());
    }

    // Consultamos una película por su identificador.
    @Override
    public MovieDTO getById(long id) {
        MovieEntity movieEntity = this.crudMovieEntity
                .findById(id)
                .orElse(null);

        return this.movieMapper.toDTO(movieEntity);
    }

    // Creamos una película.
    @Override
    public MovieDTO save(MovieDTO movieDTO) {
        MovieEntity movieEntity = this.movieMapper.toEntity(movieDTO);

        movieEntity.setEstado("0");

        MovieEntity savedMovie = this.crudMovieEntity.save(movieEntity);

        return this.movieMapper.toDTO(savedMovie);
    }

    // Actualizamos una película existente.
    @Override
    public MovieDTO update(long id, UpdateMovieDTO updateMovieDTO) {
        MovieEntity movieEntity = this.crudMovieEntity
                .findById(id)
                .orElse(null);

        // Si no existe, devolvemos null.
        if (movieEntity == null) {
            return null;
        }

        // Aplicamos los cambios sobre la entidad encontrada.
        this.movieMapper.updateEntityFromDTO(updateMovieDTO, movieEntity);
        MovieEntity updatedMovie = this.crudMovieEntity.save(movieEntity);
        return this.movieMapper.toDTO(updatedMovie);
    }

    // Eliminamos la película de la base de datos.
    @Override
    public boolean delete(long id) {

        // Comprobamos que la película existe.
        if (!this.crudMovieEntity.existsById(id)) {
            return false;
        }

        this.crudMovieEntity.deleteById(id);

        return true;
    }
}