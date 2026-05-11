package com.botagendamento.repository;

import com.botagendamento.entity.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
    List<BusinessHours> findByDayOfWeekAndActiveTrue(DayOfWeek dayOfWeek);
}
