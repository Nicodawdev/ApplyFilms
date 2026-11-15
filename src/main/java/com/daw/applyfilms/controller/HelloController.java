package com.daw.applyfilms.controller;

import com.daw.applyfilms.service.ApplyFilmsAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Bienvenida", description = "Saludo generado con IA")
public class HelloController {

    private final String plataforma;
    private final ApplyFilmsAiService aiService;

    public HelloController(@Value("${spring.application.name}") String plataforma, ApplyFilmsAiService aiService) {
        this.plataforma = plataforma;
        this.aiService = aiService;
    }

    @GetMapping("/hello")
    @Operation(summary = "Saludo de bienvenida", description = "Genera con IA un saludo de bienvenida a la plataforma")
    @ApiResponse(responseCode = "200", description = "Saludo generado")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(aiService.generarSaludo(plataforma));
    }
}
