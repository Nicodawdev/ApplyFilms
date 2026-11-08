package com.applyfilms.web.exception;

import com.applyfilms.domain.exception.MovieAlreadyExistException;
import com.applyfilms.domain.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Gestionamos las excepciones de la API.
@RestControllerAdvice
public class RestExceptionHandler {

    // Respondemos cuando el título de una película ya existe.
    @ExceptionHandler(MovieAlreadyExistException.class)
    public ResponseEntity<Error> handleMovieAlreadyExist(
            MovieAlreadyExistException ex
    ) {
        Error error = new Error("movie-already-exist", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    // Respondemos cuando la película no existe.
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Error> handleMovieNotFound(
            MovieNotFoundException ex
    ) {
        Error error = new Error("movie-not-found", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
