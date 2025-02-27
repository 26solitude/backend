package com.burntoburn.easyshift.service.shift.imp;

import com.burntoburn.easyshift.dto.shift.req.ShiftUpload;
import com.burntoburn.easyshift.entity.schedule.Schedule;
import com.burntoburn.easyshift.entity.schedule.Shift;
import com.burntoburn.easyshift.entity.user.User;
import com.burntoburn.easyshift.repository.schedule.ScheduleRepository;
import com.burntoburn.easyshift.repository.schedule.ShiftRepository;
import com.burntoburn.easyshift.service.shift.ShiftService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ShiftServiceImpTest {

    @Autowired
    private ShiftService shiftService; // 실제 서비스 객체

    @MockitoBean
    private ShiftRepository shiftRepository; // Mock Repository

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("Shift 생성 테스트")
    void createShiftTest() {
        // Given
        Schedule schedule = Schedule.builder().id(2L).build();
        ShiftUpload shiftUpload = ShiftUpload.builder()
                .shiftName("Evening Shift")
                .shiftDate(LocalDate.of(2024, 2, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(22, 0))
                .build();

        Shift shiftToSave = Shift.builder()
                .shiftName(shiftUpload.getShiftName())
                .shiftDate(shiftUpload.getShiftDate())
                .startTime(shiftUpload.getStartTime())
                .endTime(shiftUpload.getEndTime())
                .schedule(schedule)
                .build();

        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(schedule));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shiftToSave);

        // When
        Shift createdShift = shiftService.createShift(2L, shiftUpload);

        // Then
        assertNotNull(createdShift);
        assertEquals("Evening Shift", createdShift.getShiftName());
        verify(scheduleRepository, times(1)).findById(2L);
        verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    @DisplayName("Shift 단건 조회 테스트")
    void getShiftOneTest() {
        // Given
        Shift shift = Shift.builder()
                .id(1L)
                .shiftName("Morning Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .build();

        when(shiftRepository.getShiftById(1L)).thenReturn(Optional.of(shift));

        // When
        Shift foundShift = shiftService.getShiftOne(1L);

        // Then
        assertNotNull(foundShift);
        assertEquals("Morning Shift", foundShift.getShiftName());
        verify(shiftRepository, times(1)).getShiftById(1L);
    }

    @Test
    @DisplayName("Shift 전체 조회 테스트")
    void getAllShiftsTest() {
        // Given
        Shift shift1 = Shift.builder()
                .id(1L)
                .shiftName("Morning Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .build();
        Shift shift2 = Shift.builder()
                .id(2L)
                .shiftName("Afternoon Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(21, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(2L).build())
                .build();
        List<Shift> shiftList = Arrays.asList(shift1, shift2);

        when(shiftRepository.findAll()).thenReturn(shiftList);

        // When
        List<Shift> shifts = shiftService.getAllShifts();

        // Then
        assertNotNull(shifts);
        assertEquals(2, shifts.size());
        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Shift 업데이트 테스트")
    void updateShiftTest() {
        // Given
        Shift existingShift = Shift.builder()
                .id(1L)
                .shiftName("Morning Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .build();

        ShiftUpload updateUpload = ShiftUpload.builder()
                .shiftName("Evening Shift")
                .shiftDate(LocalDate.of(2024, 2, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(22, 0))
                .build();

        // 기존 Shift 조회 Mock 설정
        when(shiftRepository.getShiftById(1L)).thenReturn(Optional.of(existingShift));

        // updateShift() 내부에서 기존 엔티티의 updateShift() 메서드를 호출 후 save()하는 구조이므로,
        // save() 호출 시 업데이트된 데이터를 반환하도록 설정
        Shift updatedShift = Shift.builder()
                .id(1L)
                .shiftName(updateUpload.getShiftName())
                .shiftDate(updateUpload.getShiftDate())
                .startTime(updateUpload.getStartTime())
                .endTime(updateUpload.getEndTime())
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .build();

        when(shiftRepository.save(any(Shift.class))).thenReturn(updatedShift);

        // When
        Shift result = shiftService.updateShift(1L, updateUpload);

        // Then
        assertNotNull(result);
        assertEquals("Evening Shift", result.getShiftName());
        //verify(shiftRepository, times(1)).getShiftById(1L);
        //verify(shiftRepository, times(1)).save(any(Shift.class));
    }

    @Test
    @DisplayName("Shift 단건 조회 (User 포함) 테스트")
    void getShiftWithUserTest() {
        // Given
        Shift shift = Shift.builder()
                .id(1L)
                .shiftName("Morning Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(1L).name("johndoe").email("john@example.com").build())
                .build();

        when(shiftRepository.findByIdWithUser(1L)).thenReturn(Optional.of(shift));

        // When
        Shift foundShift = shiftService.getShiftWithUser(1L);

        // Then
        assertNotNull(foundShift);
        assertEquals("Morning Shift", foundShift.getShiftName());
        assertNotNull(foundShift.getUser());
        assertEquals("johndoe", foundShift.getUser().getName());
        verify(shiftRepository, times(1)).findByIdWithUser(1L);
    }

    @Test
    @DisplayName("Shift 삭제 테스트")
    void deleteShiftTest() {
        // Given
        Long shiftId = 1L;
        Shift shiftToDelete = Shift.builder()
                .id(shiftId)
                .shiftName("Night Shift")
                .shiftDate(LocalDate.of(2024, 2, 19))
                .startTime(LocalTime.of(21, 0))
                .endTime(LocalTime.of(5, 0))
                .schedule(Schedule.builder().id(1L).build())
                .user(User.builder().id(3L).build())
                .build();

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shiftToDelete));
        doNothing().when(shiftRepository).deleteById(shiftId);

        // When
        shiftService.deleteShift(shiftId);

        // Then
        verify(shiftRepository, times(1)).findById(shiftId);
        verify(shiftRepository, times(1)).deleteById(shiftId);
    }
}
