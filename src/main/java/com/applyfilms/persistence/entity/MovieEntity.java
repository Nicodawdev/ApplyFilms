// Paquete donde organizamos las entidades de la base de datos.
package com.applyfilms.persistence.entity;

// Anotaciones utilizadas para relacionar la clase con la tabla.
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Tipos utilizados para los valores decimales y las fechas.
import java.math.BigDecimal;
import java.time.LocalDate;

// Indicamos que esta clase representa una entidad de JPA.
@Entity
// Especificamos el nombre de la tabla en la base de datos.
@Table(name = "apply_films_peliculas")
public class MovieEntity {

    // Identificador único de la película, generado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título obligatorio, de hasta 150 caracteres y sin duplicados.
    @Column(nullable = false, length = 150, unique = true)
    private String titulo;

    // Duración obligatoria de la película, expresada en minutos.
    @Column(nullable = false)
    private Integer duracion;

    // Género obligatorio, de hasta 40 caracteres.
    @Column(nullable = false, length = 40)
    private String genero;

    // Relacionamos fechaEstreno con la columna fecha_estreno.
    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    // Clasificación con tres dígitos en total y dos decimales.
    // Permite valores nulos, como los incluidos en nuestro data.sql.
    @Column(precision = 3, scale = 2)
    private BigDecimal clasificacion;

    // Estado obligatorio, representado mediante un único carácter.
    @Column(nullable = false, length = 1)
    private String estado;

    // Constructor vacío utilizado por JPA para crear las entidades.
    public MovieEntity() {
    }

    // Obtenemos el identificador de la película.
    public Long getId() {
        return id;
    }

    // Asignamos el identificador de la película.
    public void setId(Long id) {
        this.id = id;
    }

    // Obtenemos el título de la película.
    public String getTitulo() {
        return titulo;
    }

    // Asignamos el título de la película.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Obtenemos la duración de la película.
    public Integer getDuracion() {
        return duracion;
    }

    // Asignamos la duración de la película.
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    // Obtenemos el género de la película.
    public String getGenero() {
        return genero;
    }

    // Asignamos el género de la película.
    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Obtenemos la fecha de estreno.
    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    // Asignamos la fecha de estreno.
    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    // Obtenemos la clasificación de la película.
    public BigDecimal getClasificacion() {
        return clasificacion;
    }

    // Asignamos la clasificación de la película.
    public void setClasificacion(BigDecimal clasificacion) {
        this.clasificacion = clasificacion;
    }

    // Obtenemos el estado de la película.
    public String getEstado() {
        return estado;
    }

    // Asignamos el estado de la película.
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
