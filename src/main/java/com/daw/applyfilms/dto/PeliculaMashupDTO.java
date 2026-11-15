package com.daw.applyfilms.dto;

import com.daw.applyfilms.model.Genero;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Respuesta híbrida: datos de nuestra BD local + datos de la API externa TMDB")
public record PeliculaMashupDTO(
        @Schema(description = "[Local] Identificador", example = "1") Long id,
        @Schema(description = "[Local] Título", example = "Interstellar") String titulo,
        @Schema(description = "[Local] Género", example = "CIENCIA_FICCION") Genero genero,
        @Schema(description = "[Local] Fecha de estreno", example = "2014-11-07") LocalDate fechaEstreno,
        @Schema(description = "[Local] Nuestra valoración (0-5)", example = "4.8") Double clasificacion,
        @Schema(description = "[TMDB] Título original", example = "Interstellar") String tituloOriginal,
        @Schema(description = "[TMDB] Sinopsis", example = "Un grupo de exploradores viaja a través de un agujero de gusano...") String sinopsis,
        @Schema(description = "[TMDB] Nota media de los usuarios de TMDB (0-10)", example = "8.4") Double notaTmdb,
        @Schema(description = "[TMDB] Número de votos en TMDB", example = "36000") Integer votosTmdb,
        @Schema(description = "[TMDB] URL del póster", example = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg") String posterUrl,
        @Schema(description = "Origen de los datos externos (TMDB o NO DISPONIBLE)", example = "TMDB") String fuenteExterna
) {
}
