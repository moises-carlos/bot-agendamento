package com.botagendamento.service;

import com.botagendamento.dto.AppointmentRequest;
import com.botagendamento.dto.AppointmentResponse;
import com.botagendamento.entity.Appointment;
import com.botagendamento.entity.Client;
import com.botagendamento.entity.ServiceType;
import com.botagendamento.entity.enums.AppointmentStatus;
import com.botagendamento.exception.BusinessException;
import com.botagendamento.exception.ResourceNotFoundException;
import com.botagendamento.repository.AppointmentRepository;
import com.botagendamento.repository.ClientRepository;
import com.botagendamento.repository.ServiceTypeRepository;
import com.botagendamento.service.integration.CalendarIntegrationService;
import com.botagendamento.service.integration.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final AvailabilityService availabilityService;
    private final CalendarIntegrationService calendarIntegrationService;
    private final WhatsAppService whatsAppService;

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        ServiceType serviceType = serviceTypeRepository.findById(request.serviceTypeId())
            .filter(ServiceType::isActive)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        LocalDateTime start = request.startTime();
        LocalDateTime end = start.plusMinutes(serviceType.getDurationMinutes() + serviceType.getBufferMinutes());

        availabilityService.validateSlot(start, end);
        ensureNoConflicts(start, end);

        Client client = clientRepository.findByPhone(request.clientPhone())
            .map(existing -> updateClient(existing, request))
            .orElseGet(() -> createClient(request));

        Appointment appointment = Appointment.builder()
            .client(client)
            .serviceType(serviceType)
            .startTime(start)
            .endTime(end)
            .status(AppointmentStatus.CONFIRMED)
            .notes(request.notes())
            .build();

        appointment = appointmentRepository.save(appointment);
        appointment.setGoogleEventId(calendarIntegrationService.createEvent(appointment));
        appointment = appointmentRepository.save(appointment);

        whatsAppService.sendMessage(client.getPhone(),
            "Seu agendamento está confirmado para " + appointment.getStartTime());

        return toResponse(appointment);
    }

    public List<AppointmentResponse> listByDay(LocalDateTime dayStart) {
        LocalDateTime from = dayStart.toLocalDate().atStartOfDay();
        LocalDateTime to = from.plusDays(1);
        return appointmentRepository.findByStartTimeBetween(from, to)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    private void ensureNoConflicts(LocalDateTime start, LocalDateTime end) {
        boolean conflict = appointmentRepository.existsByStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
            end,
            start,
            List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED, AppointmentStatus.RESCHEDULED)
        );
        if (conflict) {
            throw new BusinessException("Horário indisponível");
        }
    }

    private Client updateClient(Client client, AppointmentRequest request) {
        client.setFullName(request.clientName());
        client.setEmail(request.clientEmail());
        client.setRecurring(true);
        return clientRepository.save(client);
    }

    private Client createClient(AppointmentRequest request) {
        Client client = Client.builder()
            .fullName(request.clientName())
            .phone(request.clientPhone())
            .email(request.clientEmail())
            .recurring(false)
            .build();
        return clientRepository.save(client);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getClient().getFullName(),
            appointment.getClient().getPhone(),
            appointment.getServiceType().getName(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            appointment.getStatus());
    }
}
