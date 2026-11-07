// Entidad que relaciona los atributos Java con las columnas de la tabla.
package com.applyfilms.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ApplyFilms_peliculas")
public class MovieEntity {

    // Identificador generado automáticamente por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título obligatorio y único, con un máximo de 150 caracteres.
    @Column(nullable = false, length = 150, unique = true)
    private String titulo;

    // Duración obligatoria, expresada en minutos.
    @Column(nullable = false)
    private Integer duracion;

    // Género obligatorio, con un máximo de 40 caracteres.
    @Column(nullable = false, length = 40)
    private String genero;

    // Nombre de la columna correspondiente a la fecha de estreno.
    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    // Clasificación con tres dígitos en total y dos decimales.
    @Column(precision = 3, scale = 2)
    private BigDecimal clasificacion;

    // Estado obligatorio, representado mediante un carácter.
    @Column(nullable = false, length = 1)
    private String estado;

    // Constructor vacío requerido por JPA.
    public MovieEntity() {
    }

    // Consultamos y modificamos el identificador.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Consultamos y modificamos el título.
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Consultamos y modificamos la duración.
    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    // Consultamos y modificamos el género.
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Consultamos y modificamos la fecha de estreno.
    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    // Consultamos y modificamos la clasificación.
    public BigDecimal getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(BigDecimal clasificacion) {
        this.clasificacion = clasificacion;
    }

    // Consultamos el estado de esta película.
    public String getEstado() {
        return estado;
    }

    // Modificamos el estado de esta película.
    // Al ser un método de instancia, podemos utilizar this.
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
