package com.daw.applyfilms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Preferencias del usuario para pedir recomendaciones a la IA")
public record RecomendacionRequestDTO(
        @Schema(description = "Gustos del usuario", example = "Me gustan las películas de ciencia ficción con buena nota")
        @NotBlank(message = "Las preferencias son obligatorias")
        String preferencias
) {
}
