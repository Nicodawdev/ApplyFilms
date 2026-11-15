package com.daw.applyfilms.exception;

import com.daw.applyfilms.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ManejadorExcepcionesGlobal {

    // 400: errores de @Valid, indicando qué campo falló y por qué.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampo = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> erroresCampo.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse error = new ErrorResponse(400, "Hay errores de validación en los datos enviados", erroresCampo);
        return ResponseEntity.badRequest().body(error);
    }

    // 400: JSON mal formado o con valores imposibles (por ejemplo un género que no existe).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonIncorrecto(HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse(400, "El cuerpo de la petición no es un JSON válido o tiene valores incorrectos");
        return ResponseEntity.badRequest().body(error);
    }

    // 400: parámetro de la URL con un tipo incorrecto (por ejemplo /api/peliculas/abc).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse(400, "El parámetro '" + ex.getName() + "' tiene un valor no válido");
        return ResponseEntity.badRequest().body(error);
    }

    // 404: la película no existe.
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 404: la ruta pedida no existe.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaNoEncontrada(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "La ruta /" + ex.getResourcePath() + " no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 409: se incumple una restricción de la BD (por ejemplo, título duplicado).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse(409, "Conflicto con los datos existentes: ya existe una película con ese título");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 422: se incumple una regla de negocio.
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(ReglaNegocioException ex) {
        ErrorResponse error = new ErrorResponse(422, ex.getMessage());
        return ResponseEntity.status(422).body(error);
    }

    // 500: cualquier error no previsto. No se envía la traza al cliente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGenerico(Exception ex) {
        log.error("Error inesperado", ex);
        ErrorResponse error = new ErrorResponse(500, "Error interno del servidor");
        return ResponseEntity.internalServerError().body(error);
    }
}
