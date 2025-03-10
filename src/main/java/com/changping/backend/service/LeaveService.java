package com.changping.backend.service;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;

import java.util.List;

public interface LeaveService {

    // 通过学生姓名获取请假请求
    public List<LeaveRequestDTO> getLeaveRequestByName(String name);
}
