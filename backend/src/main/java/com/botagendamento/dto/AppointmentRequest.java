package com.botagendamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(
    @NotBlank String clientName,
    @NotBlank String clientPhone,
    @Email String clientEmail,
    @NotNull Long serviceTypeId,
    @Future @NotNull LocalDateTime startTime,
    String notes
) {
}
