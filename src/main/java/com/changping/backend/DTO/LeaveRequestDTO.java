package com.changping.backend.DTO;

import com.changping.backend.entity.LeaveRequest;
import lombok.Data;

import java.sql.Date;

@Data
public class LeaveRequestDTO {
    private int id;
    private String name;
    private String classNum;
    private String studentId;
    private String leaveType;
    private Date leaveDate;
    private String remarks;

}
