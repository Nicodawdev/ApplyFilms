package com.applyfilms.domain.repository;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.persistence.entity.MovieEntity;

import java.util.List;

public interface MovieRepository {
    List<MovieDTO> getAll();
    MovieDTO getById(long id);
}
