package com.botagendamento.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.botagendamento.service.integration.NlpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/whatsapp")
@RequiredArgsConstructor
public class WebhookController {

    private final NlpService nlpService;
    private final ObjectMapper objectMapper;

    @Value("${app.webhook.token:change-me}")
    private String webhookToken;
    @Value("${app.webhook.app-secret:change-me-secret}")
    private String appSecret;

    @GetMapping
    public ResponseEntity<String> verify(@RequestParam("hub.challenge") String challenge) {
        String safeChallenge = HtmlUtils.htmlEscape(challenge).replaceAll("[\\r\\n]", "");
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .body(safeChallenge);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> receive(
        @RequestHeader(value = "X-Webhook-Token", required = false) String token,
        @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
        @RequestBody String rawPayload
    ) throws Exception {
        if (token == null || !token.equals(webhookToken) || !isValidSignature(rawPayload, signature)) {
            return ResponseEntity.status(401).body(Map.of("status", "unauthorized"));
        }
        Map<String, Object> payload = objectMapper.readValue(rawPayload, new TypeReference<>() {});
        String message = String.valueOf(payload.getOrDefault("message", ""));
        String intent = nlpService.detectIntent(message);
        return ResponseEntity.ok(Map.of("status", "received", "intent", intent));
    }

    private boolean isValidSignature(String rawPayload, String signature) throws Exception {
        if (signature == null || signature.isBlank()) {
            return false;
        }
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] digest = mac.doFinal(rawPayload.getBytes(StandardCharsets.UTF_8));
        String expected = "sha256=" + bytesToHex(digest);
        return expected.equals(signature);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
