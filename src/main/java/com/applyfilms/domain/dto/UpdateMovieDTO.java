package com.applyfilms.domain.dto;

import java.time.LocalDate;

// Datos que podemos actualizar de una película.
public record UpdateMovieDTO(
        String title,
        LocalDate releaseDate,
        Double rating
) {
}
