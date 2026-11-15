package com.daw.applyfilms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Respuesta del endpoint /search/movie de TMDB (solo los campos que usamos).
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbRespuestaDTO(List<Resultado> results) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Resultado(
            @JsonProperty("original_title") String tituloOriginal,
            @JsonProperty("overview") String sinopsis,
            @JsonProperty("vote_average") Double notaMedia,
            @JsonProperty("vote_count") Integer votos,
            @JsonProperty("poster_path") String poster
    ) {
    }
}
