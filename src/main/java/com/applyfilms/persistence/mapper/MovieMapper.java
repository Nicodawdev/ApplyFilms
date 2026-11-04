package com.applyfilms.persistence.mapper;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.persistence.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

   @Mapping(source = "titulo", target = "title")
   @Mapping(source = "duracion", target = "duration")
   @Mapping(source = "genero", target = "genre")
   @Mapping(source = "fechaEstreno", target ="releaseDate")
   @Mapping(source = "clasificacion", target ="rating")
    MovieDTO toDTO(MovieEntity entity);
    List<MovieDTO> toDTO(Iterable<MovieEntity> entities);
}
