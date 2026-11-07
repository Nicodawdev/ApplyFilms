package com.applyfilms.persistence.mapper;

import com.applyfilms.domain.Genre;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

// Convertimos los géneros entre la base de datos y el dominio.
@Component
public class GenreMapper {

    // Convertimos el texto de la base de datos al enum.
    @Named("stringToGenre")
    public Genre stringToGenre(String genero) {
        if (genero == null) {
            return null;
        }

        return switch (genero) {
            case "ACCION" -> Genre.ACTION;
            case "COMEDIA" -> Genre.COMEDY;
            case "DRAMA" -> Genre.DRAMA;
            case "ANIMADA" -> Genre.ANIMATED;
            case "TERROR" -> Genre.HORROR;
            case "CIENCIA_FICCION" -> Genre.SCI_FI;
            default -> throw new IllegalArgumentException(
                    "Género no reconocido: " + genero
            );
        };
    }

    // Convertimos el enum al texto utilizado en la base de datos.
    @Named("genreToString")
    public String genreToString(Genre genre) {
        if (genre == null) {
            return null;
        }

        return switch (genre) {
            case ACTION -> "ACCION";
            case COMEDY -> "COMEDIA";
            case DRAMA -> "DRAMA";
            case ANIMATED -> "ANIMADA";
            case HORROR -> "TERROR";
            case SCI_FI -> "CIENCIA_FICCION";
        };
    }
}
