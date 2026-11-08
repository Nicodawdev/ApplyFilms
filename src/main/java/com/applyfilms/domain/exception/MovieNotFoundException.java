package com.applyfilms.domain.exception;

// Excepción para una película que no existe.
public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(long id) {
        super("La película con ID " + id + " no existe");
    }
}
