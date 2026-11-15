package com.daw.applyfilms.service;

import com.daw.applyfilms.dto.PeliculaCreacionDTO;
import com.daw.applyfilms.dto.PeliculaMashupDTO;
import com.daw.applyfilms.dto.PeliculaPatchDTO;
import com.daw.applyfilms.dto.PeliculaResumenDTO;
import com.daw.applyfilms.dto.TmdbRespuestaDTO;
import com.daw.applyfilms.exception.RecursoNoEncontradoException;
import com.daw.applyfilms.exception.ReglaNegocioException;
import com.daw.applyfilms.model.Pelicula;
import com.daw.applyfilms.repository.PeliculaRepository;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final ExternalApiService externalApiService;

    @Value("${tmdb.image-url}")
    private String urlImagenes;

    @Tool("Busca todas las películas que existen dentro de la plataforma")
    public List<PeliculaResumenDTO> obtenerTodas() {
        List<PeliculaResumenDTO> peliculas = peliculaRepository.findAll().stream()
                .map(this::aResumenDTO)
                .toList();
        log.info("[LISTAR] Se han obtenido {} películas", peliculas.size());
        return peliculas;
    }

    public PeliculaResumenDTO obtenerPorId(Long id) {
        PeliculaResumenDTO pelicula = aResumenDTO(buscarEntidad(id));
        log.info("[CONSULTAR] Película id={} encontrada: '{}'", id, pelicula.titulo());
        return pelicula;
    }

    public List<PeliculaResumenDTO> buscarPorTitulo(String texto) {
        List<PeliculaResumenDTO> peliculas = peliculaRepository.findByTituloContainingIgnoreCase(texto).stream()
                .map(this::aResumenDTO)
                .toList();
        log.info("[BUSCAR] Título que contiene '{}': {} resultados", texto, peliculas.size());
        return peliculas;
    }

    public List<PeliculaResumenDTO> buscarPorClasificacionMinima(Double minimo) {
        List<PeliculaResumenDTO> peliculas = peliculaRepository.findByClasificacionGreaterThanEqual(minimo).stream()
                .map(this::aResumenDTO)
                .toList();
        log.info("[BUSCAR] Clasificación >= {}: {} resultados", minimo, peliculas.size());
        return peliculas;
    }

    @CacheEvict(value = "mashup", allEntries = true)
    public PeliculaResumenDTO crear(PeliculaCreacionDTO dto) {
        Pelicula pelicula = new Pelicula();
        copiarDatos(dto, pelicula);
        Pelicula guardada = peliculaRepository.saveAndFlush(pelicula);
        log.info("[CREAR] Película creada con id={}: '{}'. Caché 'mashup' vaciada", guardada.getId(), guardada.getTitulo());
        return aResumenDTO(guardada);
    }

    @CacheEvict(value = "mashup", allEntries = true)
    public PeliculaResumenDTO reemplazar(Long id, PeliculaCreacionDTO dto) {
        Pelicula pelicula = buscarEntidad(id);
        String tituloAnterior = pelicula.getTitulo();
        copiarDatos(dto, pelicula);
        Pelicula guardada = peliculaRepository.saveAndFlush(pelicula);
        log.info("[REEMPLAZAR] Película id={} reemplazada: '{}' -> '{}'. Caché 'mashup' vaciada",
                id, tituloAnterior, guardada.getTitulo());
        return aResumenDTO(guardada);
    }

    @CacheEvict(value = "mashup", allEntries = true)
    public PeliculaResumenDTO actualizarParcial(Long id, PeliculaPatchDTO dto) {
        Pelicula pelicula = buscarEntidad(id);
        log.info("[ACTUALIZAR] Modificando parcialmente la película id={} con los campos: {}", id, dto);

        if (dto.titulo() != null) pelicula.setTitulo(dto.titulo());
        if (dto.duracion() != null) pelicula.setDuracion(dto.duracion());
        if (dto.genero() != null) pelicula.setGenero(dto.genero());
        if (dto.fechaEstreno() != null) pelicula.setFechaEstreno(dto.fechaEstreno());
        if (dto.clasificacion() != null) pelicula.setClasificacion(dto.clasificacion());
        if (dto.idioma() != null) pelicula.setIdioma(dto.idioma());
        if (dto.emailDistribuidora() != null) pelicula.setEmailDistribuidora(dto.emailDistribuidora());
        if (dto.disponible() != null) pelicula.setDisponible(dto.disponible());
        if (dto.etiquetas() != null) pelicula.setEtiquetas(new ArrayList<>(dto.etiquetas()));

        Pelicula guardada = peliculaRepository.saveAndFlush(pelicula);
        log.info("[ACTUALIZAR] Película id={} ('{}') actualizada. Caché 'mashup' vaciada", id, guardada.getTitulo());
        return aResumenDTO(guardada);
    }

    // Regla de negocio: solo se pueden borrar películas que ya no están disponibles en el catálogo.
    @CacheEvict(value = "mashup", allEntries = true)
    public void eliminar(Long id) {
        Pelicula pelicula = buscarEntidad(id);
        if (Boolean.TRUE.equals(pelicula.getDisponible())) {
            log.warn("[ELIMINAR] Rechazado: la película id={} ('{}') está disponible", id, pelicula.getTitulo());
            throw new ReglaNegocioException(
                    "No se puede eliminar '" + pelicula.getTitulo() + "' porque está disponible. Márcala como no disponible primero");
        }
        peliculaRepository.delete(pelicula);
        log.info("[ELIMINAR] Película id={} ('{}') eliminada. Caché 'mashup' vaciada", id, pelicula.getTitulo());
    }

    // Mashup: une los datos de nuestra BD con los de TMDB. El resultado se guarda en caché.
    @Cacheable(value = "mashup", key = "#id")
    public PeliculaMashupDTO obtenerMashup(Long id) {
        // Este log solo aparece si el mashup NO estaba en caché: si lo está, Spring no ejecuta el método.
        log.info("[MASHUP] id={} NO está en caché -> consultando BD H2 + API de TMDB", id);
        Pelicula pelicula = buscarEntidad(id);
        TmdbRespuestaDTO.Resultado tmdb = externalApiService.buscarPelicula(
                pelicula.getTitulo(), pelicula.getFechaEstreno().getYear());

        if (tmdb == null) {
            return new PeliculaMashupDTO(pelicula.getId(), pelicula.getTitulo(), pelicula.getGenero(),
                    pelicula.getFechaEstreno(), pelicula.getClasificacion(),
                    "No disponible", "No disponible", 0.0, 0, null, "NO DISPONIBLE");
        }

        String posterUrl = tmdb.poster() != null ? urlImagenes + tmdb.poster() : null;
        return new PeliculaMashupDTO(pelicula.getId(), pelicula.getTitulo(), pelicula.getGenero(),
                pelicula.getFechaEstreno(), pelicula.getClasificacion(),
                tmdb.tituloOriginal(), tmdb.sinopsis(), tmdb.notaMedia(), tmdb.votos(), posterUrl, "TMDB");
    }

    private Pelicula buscarEntidad(Long id) {
        return peliculaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[NO ENCONTRADA] No existe ninguna película con id={}", id);
                    return new RecursoNoEncontradoException(id);
                });
    }

    private void copiarDatos(PeliculaCreacionDTO dto, Pelicula pelicula) {
        pelicula.setTitulo(dto.titulo());
        pelicula.setDuracion(dto.duracion());
        pelicula.setGenero(dto.genero());
        pelicula.setFechaEstreno(dto.fechaEstreno());
        pelicula.setClasificacion(dto.clasificacion());
        pelicula.setIdioma(dto.idioma());
        pelicula.setEmailDistribuidora(dto.emailDistribuidora());
        pelicula.setDisponible(dto.disponible());
        pelicula.setEtiquetas(dto.etiquetas() != null ? new ArrayList<>(dto.etiquetas()) : new ArrayList<>());
    }

    private PeliculaResumenDTO aResumenDTO(Pelicula pelicula) {
        return new PeliculaResumenDTO(
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getDuracion(),
                pelicula.getGenero(),
                pelicula.getFechaEstreno(),
                pelicula.getClasificacion(),
                pelicula.getIdioma(),
                pelicula.getEmailDistribuidora(),
                pelicula.getDisponible(),
                List.copyOf(pelicula.getEtiquetas()));
    }
}
