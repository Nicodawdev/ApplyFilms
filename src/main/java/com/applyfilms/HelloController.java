package com.applyfilms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final String plataforma;
    private final ApplyFilmsAiService aiService;

    public HelloController(@Value("${spring.application.name}") String plataforma, ApplyFilmsAiService aiService) {
        this.plataforma = plataforma;
        this.aiService = aiService;
    }
    @GetMapping("/")
    public String hello() {
        return this.aiService.generateGreeting(plataforma);
    }
}
