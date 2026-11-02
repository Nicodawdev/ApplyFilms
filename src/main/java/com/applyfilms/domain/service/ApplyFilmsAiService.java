// Paquete donde organizamos los servicios del dominio.
package com.applyfilms.domain.service;
// Permite definir el mensaje enviado al modelo.
import dev.langchain4j.service.UserMessage;
// Permite identificar las variables utilizadas dentro del mensaje.
import dev.langchain4j.service.V;
// Integra nuestra interfaz de IA con Spring.
import dev.langchain4j.service.spring.AiService;
// LangChain4j creará la implementación de esta interfaz.
@AiService
public interface ApplyFilmsAiService {

    // Definimos las instrucciones para generar el saludo.
    // {{plataforma}} se sustituirá por el parámetro recibido.
    @UserMessage("""
            Genera un saludo de bienvenida en español para la plataforma
            de gestión de películas "{{plataforma}}".
            Utiliza un máximo de 180 caracteres y devuelve únicamente el saludo.
            """)
    // El método recibe el nombre de la plataforma y devuelve el saludo.
    // @V relaciona el parámetro con la variable del mensaje.
    String generateGreeting(@V("plataforma") String plataforma);
}
