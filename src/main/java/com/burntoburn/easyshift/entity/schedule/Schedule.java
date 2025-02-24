package com.burntoburn.easyshift.entity.schedule;

import com.burntoburn.easyshift.entity.BaseEntity;
import com.burntoburn.easyshift.entity.schedule.collection.Shifts;
import com.burntoburn.easyshift.entity.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "schedule") // 테이블명 명시
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (protected)
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Builder // Lombok Builder 적용
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // ID는 자동 생성되므로 Builder에서 제외
    private Long id;

    @Column(nullable = false)
    private String scheduleName;

    // 예: "2024-11" 형식으로 월 정보를 저장
    @Column(nullable = false)
    private String scheduleMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus scheduleStatus; // PENDING, COMPLETED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Embedded
    @Builder.Default // 기본값 설정
    private Shifts shifts = new Shifts(); // 일급 컬렉션 적용

    // 스케줄 업데이트 메서드
    public void updateSchedule(String scheduleName, String scheduleMonth, List<Shift> newShifts) {
        this.scheduleName = scheduleName;
        this.scheduleMonth = scheduleMonth;
        this.shifts.update(newShifts); // 일급 컬렉션 내부에서 관리
    }

    public void addShift(List<Shift> newShifts){
        this.shifts.addAll(newShifts);
    }

}
