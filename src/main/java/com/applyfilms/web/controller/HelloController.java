// Paquete donde organizamos los controladores web.
package com.applyfilms.web.controller;
// Importamos la interfaz del servicio de inteligencia artificial.
import com.applyfilms.domain.service.ApplyFilmsAiService;
// Permite leer propiedades de la configuración.
import org.springframework.beans.factory.annotation.Value;
// Permite definir una ruta para peticiones HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;
// Identifica esta clase como un controlador REST.
import org.springframework.web.bind.annotation.RestController;

// Spring utilizará esta clase para atender las peticiones web.
@RestController
public class HelloController {
    // Nombre de la aplicación, obtenido del perfil activo.
    private final String plataforma;
    // Servicio encargado de generar el saludo mediante IA.
    private final ApplyFilmsAiService aiService;

    // Spring proporciona el nombre de la plataforma y el servicio.
    public HelloController(
            @Value("${spring.application.name}") String plataforma,
            ApplyFilmsAiService aiService
    ) {
        // Guardamos el nombre de la plataforma.
        this.plataforma = plataforma;
        // Guardamos la referencia al servicio de IA.
        this.aiService = aiService;
    }

    // Asociamos la ruta raíz con este método.
    @GetMapping("/hello")

    public String hello() {
        // Solicitamos el saludo indicando el nombre de la plataforma.
        return this.aiService.generateGreeting(this.plataforma);
    }
}
