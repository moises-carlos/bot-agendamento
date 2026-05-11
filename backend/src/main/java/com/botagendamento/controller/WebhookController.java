package com.botagendamento.controller;

import com.botagendamento.service.integration.NlpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/whatsapp")
@RequiredArgsConstructor
public class WebhookController {

    private final NlpService nlpService;

    @Value("${app.webhook.token:change-me}")
    private String webhookToken;

    @GetMapping
    public ResponseEntity<String> verify(@RequestParam("hub.challenge") String challenge) {
        return ResponseEntity.ok(challenge);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> receive(@RequestHeader(value = "X-Webhook-Token", required = false) String token,
                                                       @RequestBody Map<String, Object> payload) {
        if (token == null || !token.equals(webhookToken)) {
            return ResponseEntity.status(401).body(Map.of("status", "unauthorized"));
        }
        String message = String.valueOf(payload.getOrDefault("message", ""));
        String intent = nlpService.detectIntent(message);
        return ResponseEntity.ok(Map.of("status", "received", "intent", intent));
    }
}
