package com.burntoburn.easyshift.service.leave;

import com.burntoburn.easyshift.dto.schedule.req.LeaveRequestDto;
import com.burntoburn.easyshift.entity.leave.LeaveRequest;
import com.burntoburn.easyshift.entity.schedule.Schedule;
import com.burntoburn.easyshift.entity.user.ApprovalStatus;
import com.burntoburn.easyshift.entity.user.User;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestFactory {
    public LeaveRequest createLeaveRequest(User user, Schedule schedule, LocalDate date){
        return LeaveRequest.builder()
                .user(user)
                .date(date)
                .approvalStatus(ApprovalStatus.PENDING)
                .schedule(schedule)
                .build();
    }

    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest, LocalDate date){
        return leaveRequest.updateDate(date);
    }

    public void approvedRequest(LeaveRequest leaveRequest){
        leaveRequest.approvedRequest();
    }

    public void rejectRequest(LeaveRequest leaveRequest){
        leaveRequest.rejectRequest();
    }
}
