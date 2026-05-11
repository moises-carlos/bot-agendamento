package com.botagendamento.service.integration;

import org.springframework.stereotype.Service;

@Service
public class OpenAiNlpService implements NlpService {

    @Override
    public String detectIntent(String message) {
        String normalized = message == null ? "" : message.toLowerCase();
        if (normalized.contains("cancel")) {
            return "CANCELAR";
        }
        if (normalized.contains("reagendar") || normalized.contains("reagend")) {
            return "REAGENDAR";
        }
        if (normalized.contains("humano")) {
            return "HUMANO";
        }
        return "AGENDAR";
    }
}
