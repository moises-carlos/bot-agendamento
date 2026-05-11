package com.botagendamento.service.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhatsAppCloudService implements WhatsAppService {

    @Override
    public void sendMessage(String to, String message) {
        log.info("[WhatsApp] envio para {}: {}", to, message);
    }
}
