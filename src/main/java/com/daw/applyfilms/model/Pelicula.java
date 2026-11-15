package com.daw.applyfilms.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "peliculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa una película guardada en la base de datos")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador autoincremental", example = "1")
    private Long id;

    @Column(nullable = false, length = 150, unique = true)
    @Schema(description = "Título de la película (único)", example = "Interstellar")
    private String titulo;

    @Column(nullable = false)
    @Schema(description = "Duración en minutos", example = "169")
    private Integer duracion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Género de la película", example = "CIENCIA_FICCION")
    private Genero genero;

    @Column(name = "fecha_estreno", nullable = false)
    @Schema(description = "Fecha de estreno", example = "2014-11-07")
    private LocalDate fechaEstreno;

    @Column(name = "clasificacion")
    @Schema(description = "Valoración de 0 a 5", example = "4.8")
    private Double clasificacion;

    @Column(nullable = false, length = 2)
    @Schema(description = "Idioma original (código ISO de 2 letras)", example = "en")
    private String idioma;

    @Column(name = "email_distribuidora", nullable = false, length = 100)
    @Schema(description = "Email de contacto de la distribuidora", example = "contacto@warnerbros.com")
    private String emailDistribuidora;

    @Column(nullable = false)
    @Schema(description = "Indica si la película está disponible en el catálogo", example = "true")
    private Boolean disponible;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pelicula_etiquetas", joinColumns = @JoinColumn(name = "pelicula_id"))
    @Column(name = "etiqueta", length = 30)
    @Builder.Default
    @Schema(description = "Etiquetas descriptivas", example = "[\"espacio\", \"nolan\"]")
    private List<String> etiquetas = new ArrayList<>();
}
