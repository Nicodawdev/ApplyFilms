package com.daw.applyfilms.dto;

import com.daw.applyfilms.model.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos para modificar parcialmente (PATCH) una película. Solo se cambian los campos enviados")
public record PeliculaPatchDTO(

        @Schema(description = "Nuevo título", example = "Interstellar (Edición especial)")
        @Size(min = 1, max = 150, message = "El título debe tener entre 1 y 150 caracteres")
        String titulo,

        @Schema(description = "Nueva duración en minutos", example = "175")
        @Positive(message = "La duración debe ser mayor que 0")
        Integer duracion,

        @Schema(description = "Nuevo género", example = "DRAMA")
        Genero genero,

        @Schema(description = "Nueva fecha de estreno", example = "2014-11-07")
        @PastOrPresent(message = "La fecha de estreno no puede ser futura")
        LocalDate fechaEstreno,

        @Schema(description = "Nueva valoración", example = "4.5")
        @DecimalMin(value = "0.0", message = "La clasificación no puede ser menor que 0")
        @DecimalMax(value = "5.0", message = "La clasificación no puede ser mayor que 5")
        @Digits(integer = 1, fraction = 1, message = "La clasificación debe tener como máximo 1 decimal")
        Double clasificacion,

        @Schema(description = "Nuevo idioma original", example = "es")
        @Pattern(regexp = "^[a-z]{2}$", message = "El idioma debe ser un código de 2 letras minúsculas (ej: es, en)")
        String idioma,

        @Schema(description = "Nuevo email de la distribuidora", example = "info@paramount.com")
        @Email(message = "El email de la distribuidora no tiene un formato válido")
        String emailDistribuidora,

        @Schema(description = "Nueva disponibilidad", example = "false")
        Boolean disponible,

        @Schema(description = "Nuevas etiquetas", example = "[\"clasico\"]")
        @Size(max = 5, message = "No se pueden añadir más de 5 etiquetas")
        List<String> etiquetas
) {
}
