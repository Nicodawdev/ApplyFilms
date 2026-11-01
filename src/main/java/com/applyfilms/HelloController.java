package com.applyfilms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final ApplyFilmsAiService aiService; //añadimos método al constructor

    public HelloController(ApplyFilmsAiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String hello() {
        return this.aiService.generateGreeting();
    }
}
