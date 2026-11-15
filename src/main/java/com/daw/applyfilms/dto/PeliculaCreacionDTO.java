package com.daw.applyfilms.dto;

import com.daw.applyfilms.model.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos para crear (POST) o reemplazar por completo (PUT) una película")
public record PeliculaCreacionDTO(

        @Schema(description = "Título de la película", example = "Interstellar")
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 1, max = 150, message = "El título debe tener entre 1 y 150 caracteres")
        String titulo,

        @Schema(description = "Duración en minutos", example = "169")
        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor que 0")
        Integer duracion,

        @Schema(description = "Género de la película", example = "CIENCIA_FICCION")
        @NotNull(message = "El género es obligatorio")
        Genero genero,

        @Schema(description = "Fecha de estreno (no puede ser futura)", example = "2014-11-07")
        @NotNull(message = "La fecha de estreno es obligatoria")
        @PastOrPresent(message = "La fecha de estreno no puede ser futura")
        LocalDate fechaEstreno,

        @Schema(description = "Valoración de 0.0 a 5.0 con un decimal", example = "4.8")
        @DecimalMin(value = "0.0", message = "La clasificación no puede ser menor que 0")
        @DecimalMax(value = "5.0", message = "La clasificación no puede ser mayor que 5")
        @Digits(integer = 1, fraction = 1, message = "La clasificación debe tener como máximo 1 decimal")
        Double clasificacion,

        @Schema(description = "Idioma original en código ISO de 2 letras minúsculas", example = "en")
        @NotBlank(message = "El idioma es obligatorio")
        @Pattern(regexp = "^[a-z]{2}$", message = "El idioma debe ser un código de 2 letras minúsculas (ej: es, en)")
        String idioma,

        @Schema(description = "Email de contacto de la distribuidora", example = "contacto@warnerbros.com")
        @NotBlank(message = "El email de la distribuidora es obligatorio")
        @Email(message = "El email de la distribuidora no tiene un formato válido")
        String emailDistribuidora,

        @Schema(description = "Disponible en el catálogo", example = "true")
        @NotNull(message = "Hay que indicar si la película está disponible")
        Boolean disponible,

        @Schema(description = "Etiquetas descriptivas (máximo 5)", example = "[\"espacio\", \"nolan\"]")
        @Size(max = 5, message = "No se pueden añadir más de 5 etiquetas")
        List<String> etiquetas,

        @Schema(description = "Confirmación de que se tienen los derechos de distribución", example = "true")
        @NotNull(message = "Hay que confirmar los derechos de distribución")
        @AssertTrue(message = "Debes confirmar que tienes los derechos de distribución")
        Boolean derechosConfirmados
) {
}
