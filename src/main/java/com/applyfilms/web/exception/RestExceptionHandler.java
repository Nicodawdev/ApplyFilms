package com.applyfilms.web.exception;

import com.applyfilms.domain.exception.MovieAlreadyExistException;
import com.applyfilms.domain.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    // Película duplicada.
    @ExceptionHandler(MovieAlreadyExistException.class)
    public ResponseEntity<Error> handleMovieAlreadyExist(MovieAlreadyExistException ex) {
        Error error = new Error("movie-already-exist", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // Película inexistente.
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Error> handleMovieNotFound(MovieNotFoundException ex) {
        Error error = new Error("movie-not-found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Errores de validación.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleValidation(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(new Error(fieldError.getField(), fieldError.getDefaultMessage())));
        return ResponseEntity.badRequest().body(errors);
    }
}