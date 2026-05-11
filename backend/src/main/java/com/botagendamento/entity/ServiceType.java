package com.botagendamento.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer bufferMinutes;

    @Column(nullable = false)
    private boolean active;
}
