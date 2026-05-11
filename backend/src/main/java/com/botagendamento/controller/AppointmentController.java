package com.botagendamento.controller;

import com.botagendamento.dto.AppointmentRequest;
import com.botagendamento.dto.AppointmentResponse;
import com.botagendamento.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/staff/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @GetMapping
    public List<AppointmentResponse> listByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dayStart) {
        return appointmentService.listByDay(dayStart);
    }
}
