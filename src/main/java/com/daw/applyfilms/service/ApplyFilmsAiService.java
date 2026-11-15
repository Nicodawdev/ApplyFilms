package com.daw.applyfilms.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

// LangChain4j genera la implementación de esta interfaz y la registra en Spring.
@AiService
public interface ApplyFilmsAiService {

    @UserMessage("""
            Genera un saludo de bienvenida en español para la plataforma
            de gestión de películas "{{plataforma}}".
            Utiliza un máximo de 180 caracteres y devuelve únicamente el saludo.
            """)
    String generarSaludo(@V("plataforma") String plataforma);

    @SystemMessage("""
            Eres un experto en el séptimo arte (cine).
            Antes de recomendar, consulta las películas disponibles
            utilizando la herramienta de búsqueda del catálogo.
            Recomienda un máximo de 3 películas según los gustos del usuario.
            Recomienda únicamente películas devueltas por la herramienta.
            Si no hay películas adecuadas, indícalo sin inventar títulos.
            """)
    String recomendarPeliculas(@UserMessage String preferencias);
}
