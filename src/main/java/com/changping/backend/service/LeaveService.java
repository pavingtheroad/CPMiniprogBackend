package com.changping.backend.service;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;

import java.util.List;

public interface LeaveService {

    // 通过学生姓名获取请假请求
    List<LeaveRequestDTO> getLeaveRequestByNameOrId(String name, String id);

    //上传请假申请
    void postLeaveRequest(LeaveRequest leaveRequest);

    // 根据工号获取请假请求表
    List<LeaveRequestDTO> getLeaveRequestById(String id);

    String getStaffIdByName(String name);
}
