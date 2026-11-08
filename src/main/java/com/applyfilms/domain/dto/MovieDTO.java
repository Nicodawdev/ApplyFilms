package com.applyfilms.domain.dto;

import com.applyfilms.domain.Genre;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MovieDTO(
        Long id,

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String title,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor que 0")
        Integer duration,

        @NotNull(message = "El género es obligatorio")
        Genre genre,

        @NotNull(message = "La fecha de estreno es obligatoria")
        @PastOrPresent(message = "La fecha de estreno no puede ser futura")
        LocalDate releaseDate,

        @NotNull(message = "El rating es obligatorio")
        @Min(value = 0, message = "El rating no puede ser menor que 0")
        @Max(value = 5, message = "El rating no puede ser mayor que 5")
        Double rating
) {
}
