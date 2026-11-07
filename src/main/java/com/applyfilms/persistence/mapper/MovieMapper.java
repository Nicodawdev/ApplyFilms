package com.applyfilms.persistence.mapper;

import com.applyfilms.domain.dto.MovieDTO;
import com.applyfilms.domain.dto.UpdateMovieDTO;
import com.applyfilms.persistence.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

// Convertimos las entidades y los DTO mediante MapStruct.
@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface MovieMapper {

    // Convertimos una entidad en DTO.
    @Mapping(source = "titulo", target = "title")
    @Mapping(source = "duracion", target = "duration")
    @Mapping(
            source = "genero",
            target = "genre",
            qualifiedByName = "stringToGenre"
    )
    @Mapping(source = "fechaEstreno", target = "releaseDate")
    @Mapping(source = "clasificacion", target = "rating")
    MovieDTO toDTO(MovieEntity movieEntity);

    // Convertimos una colección de entidades en DTO.
    List<MovieDTO> toDTO(Iterable<MovieEntity> movieEntities);

    // Convertimos un DTO en entidad.
    @Mapping(source = "title", target = "titulo")
    @Mapping(source = "duration", target = "duracion")
    @Mapping(
            source = "genre",
            target = "genero",
            qualifiedByName = "genreToString"
    )
    @Mapping(source = "releaseDate", target = "fechaEstreno")
    @Mapping(source = "rating", target = "clasificacion")
    @Mapping(target = "estado", ignore = true)
    MovieEntity toEntity(MovieDTO movieDTO);

    // Actualizamos los tres campos permitidos.
    @Mapping(source = "title", target = "titulo")
    @Mapping(source = "releaseDate", target = "fechaEstreno")
    @Mapping(source = "rating", target = "clasificacion")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duracion", ignore = true)
    @Mapping(target = "genero", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDTO(
            UpdateMovieDTO updateMovieDTO,
            @MappingTarget MovieEntity movieEntity
    );
}
