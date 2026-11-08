package com.applyfilms.persistence.crud;

import com.applyfilms.persistence.entity.MovieEntity;
import org.springframework.data.repository.CrudRepository;

public interface CrudMovieEntity extends CrudRepository<MovieEntity,Long>{
    MovieEntity findFirstByTitulo(String titulo);
}
