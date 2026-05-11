package com.botagendamento.service.integration;

import com.botagendamento.entity.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleCalendarService implements CalendarIntegrationService {

    @Override
    public String createEvent(Appointment appointment) {
        log.info("[GoogleCalendar] criando evento para appointmentId={} start={}", appointment.getId(), appointment.getStartTime());
        return "google-event-placeholder-" + appointment.getId();
    }

    @Override
    public void cancelEvent(String googleEventId) {
        log.info("[GoogleCalendar] cancelando evento {}", googleEventId);
    }
}
