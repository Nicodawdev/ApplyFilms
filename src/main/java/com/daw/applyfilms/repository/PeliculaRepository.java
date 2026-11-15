package com.daw.applyfilms.repository;

import com.daw.applyfilms.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    Optional<Pelicula> findByTitulo(String titulo);

    List<Pelicula> findByTituloContainingIgnoreCase(String texto);

    List<Pelicula> findByClasificacionGreaterThanEqual(Double minimo);
}
