package com.daw.applyfilms.service;

import com.daw.applyfilms.dto.TmdbRespuestaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

// Integración síncrona con la API pública The Movie Database (TMDB).
@Slf4j
@Service
public class ExternalApiService {

    private final RestClient tmdbRestClient;
    private final String apiKey;

    public ExternalApiService(RestClient tmdbRestClient, @Value("${tmdb.api-key}") String apiKey) {
        this.tmdbRestClient = tmdbRestClient;
        this.apiKey = apiKey;
    }

    // Busca una película en TMDB por título y año. Si algo falla devuelve null (fallback).
    public TmdbRespuestaDTO.Resultado buscarPelicula(String titulo, Integer anio) {
        log.info("Llamando a la API de TMDB para buscar '{}' ({})", titulo, anio);
        try {
            TmdbRespuestaDTO respuesta = tmdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("api_key", apiKey)
                            .queryParam("query", titulo)
                            .queryParam("year", anio)
                            .queryParam("language", "es-ES")
                            .build())
                    .retrieve()
                    .body(TmdbRespuestaDTO.class);

            if (respuesta == null || respuesta.results() == null || respuesta.results().isEmpty()) {
                log.warn("TMDB no ha encontrado resultados para '{}'", titulo);
                return null;
            }
            return respuesta.results().getFirst();
        } catch (Exception e) {
            log.error("No se ha podido consultar TMDB: {}", e.getMessage());
            return null;
        }
    }
}
