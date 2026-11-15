package com.daw.applyfilms.exception;

// Se lanza cuando una operación incumple una regla de negocio. Se traduce a HTTP 422.
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
