package com.botagendamento.controller;

import com.botagendamento.dto.AuthRequest;
import com.botagendamento.dto.AuthResponse;
import com.botagendamento.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerAdmin(@Valid @RequestBody AuthRequest request) {
        return authService.registerAdmin(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
