package com.botagendamento.service.integration;

import com.botagendamento.entity.Appointment;

public interface CalendarIntegrationService {
    String createEvent(Appointment appointment);

    void cancelEvent(String googleEventId);
}
