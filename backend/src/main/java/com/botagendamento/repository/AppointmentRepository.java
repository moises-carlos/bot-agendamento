package com.botagendamento.repository;

import com.botagendamento.entity.Appointment;
import com.botagendamento.entity.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
        LocalDateTime endTime,
        LocalDateTime startTime,
        Collection<AppointmentStatus> statuses
    );

    List<Appointment> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
