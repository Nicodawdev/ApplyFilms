package com.daw.applyfilms.dto;

import com.daw.applyfilms.model.Genero;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos de una película que se devuelven al cliente")
public record PeliculaResumenDTO(
        @Schema(description = "Identificador", example = "1") Long id,
        @Schema(description = "Título", example = "Interstellar") String titulo,
        @Schema(description = "Duración en minutos", example = "169") Integer duracion,
        @Schema(description = "Género", example = "CIENCIA_FICCION") Genero genero,
        @Schema(description = "Fecha de estreno", example = "2014-11-07") LocalDate fechaEstreno,
        @Schema(description = "Valoración de 0 a 5", example = "4.8") Double clasificacion,
        @Schema(description = "Idioma original", example = "en") String idioma,
        @Schema(description = "Email de la distribuidora", example = "contacto@warnerbros.com") String emailDistribuidora,
        @Schema(description = "Disponible en el catálogo", example = "true") Boolean disponible,
        @Schema(description = "Etiquetas", example = "[\"espacio\", \"nolan\"]") List<String> etiquetas
) {
}
