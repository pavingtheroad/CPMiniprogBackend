package com.changping.backend.DTO;

import com.changping.backend.entity.LeaveRequest;
import lombok.Data;

import java.sql.Date;

@Data
public class LeaveRequestDTO {
    private String name;
    private String classNum;
    private String studentId;
    private String leaveType;
    private Date leaveDate;
    private String remarks;    // 备注
    private String teacherName;

}
