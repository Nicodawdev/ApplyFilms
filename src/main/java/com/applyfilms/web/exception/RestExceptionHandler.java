package com.applyfilms.web.exception;

import com.applyfilms.domain.exception.MovieAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Gestionamos las excepciones de la API.
@RestControllerAdvice
public class RestExceptionHandler {

    // Respondemos cuando se intenta crear una película duplicada.
    @ExceptionHandler(MovieAlreadyExistException.class)
    public ResponseEntity<Error> handleException(
            MovieAlreadyExistException ex
    ) {
        Error error = new Error("movie-already-exist", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
