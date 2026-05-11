package com.botagendamento.service;

import com.botagendamento.entity.BusinessHours;
import com.botagendamento.exception.BusinessException;
import com.botagendamento.repository.BusinessHoursRepository;
import com.botagendamento.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final BusinessHoursRepository businessHoursRepository;
    private final HolidayRepository holidayRepository;

    public void validateSlot(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate date = startTime.toLocalDate();
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();

        if (holidayRepository.existsByDate(date)) {
            throw new BusinessException("Não há atendimento em feriados");
        }

        List<BusinessHours> windows = businessHoursRepository.findByDayOfWeekAndActiveTrue(dayOfWeek);
        boolean validWindow = windows.stream().anyMatch(window ->
            !startTime.toLocalTime().isBefore(window.getStartTime()) &&
                !endTime.toLocalTime().isAfter(window.getEndTime()));

        if (!validWindow) {
            throw new BusinessException("Horário fora da janela de atendimento");
        }
    }
}
