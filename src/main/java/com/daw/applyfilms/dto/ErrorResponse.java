package com.daw.applyfilms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Estructura común de todas las respuestas de error de la API")
public record ErrorResponse(
        @Schema(description = "Código de estado HTTP", example = "400") int status,
        @Schema(description = "Mensaje del error", example = "Hay errores de validación en los datos enviados") String mensaje,
        @Schema(description = "Momento en el que se produjo el error", example = "2026-11-15T10:30:00") LocalDateTime timestamp,
        @Schema(description = "Errores por campo (solo en errores de validación)", example = "{\"titulo\": \"El título es obligatorio\"}") Map<String, String> erroresCampo
) {

    public ErrorResponse(int status, String mensaje) {
        this(status, mensaje, LocalDateTime.now(), Map.of());
    }

    public ErrorResponse(int status, String mensaje, Map<String, String> erroresCampo) {
        this(status, mensaje, LocalDateTime.now(), erroresCampo);
    }
}
