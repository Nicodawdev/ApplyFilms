package com.daw.applyfilms.exception;

// Se lanza cuando no existe la película pedida. Se traduce a HTTP 404.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(Long id) {
        super("La película con ID " + id + " no existe");
    }
}
