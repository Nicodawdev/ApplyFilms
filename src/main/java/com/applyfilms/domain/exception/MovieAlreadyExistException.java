package com.applyfilms.domain.exception;

public class MovieAlreadyExistException extends RuntimeException {
    public MovieAlreadyExistException(String movieTitle) {
        super("La pelicula " + movieTitle +  " ya existe");
    }
}
