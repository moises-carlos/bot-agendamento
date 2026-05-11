package com.botagendamento.service;

import com.botagendamento.entity.BusinessHours;
import com.botagendamento.exception.BusinessException;
import com.botagendamento.repository.BusinessHoursRepository;
import com.botagendamento.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private BusinessHoursRepository businessHoursRepository;

    @Mock
    private HolidayRepository holidayRepository;

    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        availabilityService = new AvailabilityService(businessHoursRepository, holidayRepository);
    }

    @Test
    void shouldRejectHoliday() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 12, 9, 0);
        when(holidayRepository.existsByDate(start.toLocalDate())).thenReturn(true);

        assertThrows(BusinessException.class,
            () -> availabilityService.validateSlot(start, start.plusMinutes(60)));
    }

    @Test
    void shouldAllowInsideBusinessWindow() {
        LocalDateTime start = LocalDateTime.of(2026, 5, 12, 9, 0);
        when(holidayRepository.existsByDate(start.toLocalDate())).thenReturn(false);
        when(businessHoursRepository.findByDayOfWeekAndActiveTrue(DayOfWeek.TUESDAY))
            .thenReturn(List.of(BusinessHours.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .active(true)
                .build()));

        assertDoesNotThrow(() -> availabilityService.validateSlot(start, start.plusMinutes(60)));
    }
}
