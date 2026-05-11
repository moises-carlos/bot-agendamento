package com.botagendamento.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_context")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationContext {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String currentIntent;

    @Column(nullable = false)
    private String currentStep;

    @Column(length = 2000)
    private String contextJson;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
