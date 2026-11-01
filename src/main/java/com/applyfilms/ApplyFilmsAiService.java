package com.applyfilms;

// Importamos la anotación que permite definir la petición enviada a la IA
import dev.langchain4j.service.UserMessage;
// Importamos la anotación que integra el servicio de IA con Spring
import dev.langchain4j.service.spring.AiService;

// Indicamos que LangChain4j debe crear la implementación de este servicio
@AiService
public interface ApplyFilmsAiService {
    // Definimos el mensaje que se enviará al modelo cuando llamemos al método
    @UserMessage("""
            Genera un saludo de bienvenida a la plataforma de gestión de peliculas (en 180 caracteres)
            """) //Prompt
    String generateGreeting();
}

