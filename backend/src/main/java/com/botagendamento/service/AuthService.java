package com.botagendamento.service;

import com.botagendamento.dto.AuthRequest;
import com.botagendamento.dto.AuthResponse;
import com.botagendamento.entity.Role;
import com.botagendamento.entity.User;
import com.botagendamento.entity.enums.RoleName;
import com.botagendamento.exception.BusinessException;
import com.botagendamento.repository.RoleRepository;
import com.botagendamento.repository.UserRepository;
import com.botagendamento.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse registerAdmin(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("Usuário já existe");
        }

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
            .orElseThrow(() -> new BusinessException("Role ADMIN não encontrada"));

        User user = User.builder()
            .name(request.email())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .roles(Set.of(adminRole))
            .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
            saved.getEmail(), saved.getPassword(), Set.of()));
        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), Set.of()));
        return new AuthResponse(token, "Bearer");
    }
}
