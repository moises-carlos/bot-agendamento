package com.botagendamento.dto;

import com.botagendamento.entity.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
    Long id,
    String clientName,
    String clientPhone,
    String serviceName,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AppointmentStatus status
) {
}
