package com.applyfilms.domain.dto;

import com.applyfilms.domain.Genre;

import java.time.LocalDate;

public record MovieDTO(
        String title,
        Integer duration,
        Genre genre,
        LocalDate releaseDate,
        Double rating
) {
}
