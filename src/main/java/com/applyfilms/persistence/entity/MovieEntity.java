// Clase que representa una tabla en la BBDD.
// Mapeamos los atributos de Java con las columnas de la tabla.
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

    // Identificador único de cada película.
    // Se genera automáticamente en la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título obligatorio y único, con un máximo de 150 caracteres.
    @Column(nullable = false, length = 150, unique = true)
    private String titulo;

    // Duración de la película en minutos.
    @Column(nullable = false)
    private Integer duracion;

    // Género obligatorio, con un máximo de 40 caracteres.
    @Column(nullable = false, length = 40)
    private String genero;

    // Fecha de estreno de la película.
    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    // Clasificación decimal: 3 dígitos en total y 2 decimales.
    @Column(precision = 3, scale = 2)
    private BigDecimal clasificacion;

    // Estado de la película, representado por un carácter.
    @Column(nullable = false, length = 1)
    private String estado;

    // Constructor vacío requerido por JPA.
    public MovieEntity() {
    }

    // Métodos getters y setters.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public BigDecimal getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(BigDecimal clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
