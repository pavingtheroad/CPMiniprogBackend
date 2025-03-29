package com.changping.backend.service;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;
import com.changping.backend.repository.LeaveRepository;
import com.changping.backend.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService{

    private LeaveRepository leaveRepository;
    private StaffRepository staffRepository;
    public LeaveServiceImpl(LeaveRepository leaveRepository, StaffRepository staffRepository) {
        this.leaveRepository = leaveRepository;
        this.staffRepository = staffRepository;
    }

    public LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setName(leaveRequest.getName());
        dto.setClassNum(leaveRequest.getClass_num());
        dto.setStudentId(leaveRequest.getStudentId());
        dto.setLeaveType(leaveRequest.getLeave_type());
        dto.setLeaveDate(leaveRequest.getLeave_date());
        dto.setRemarks(leaveRequest.getRemarks());
        dto.setTeacherName(
                staffRepository.findByStaffId(leaveRequest.getStaffId()).getName());
        return dto;
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestByNameOrId(String name, String student_id) {
        List<LeaveRequestDTO> dtos = new ArrayList<>();
        for(LeaveRequest leaveRequest : leaveRepository.findByNameOrStudentId(name, student_id)){
            dtos.add(convertToDTO(leaveRequest));
        }
        return dtos;
    }

    @Override
    public void postLeaveRequest(LeaveRequest leaveRequest) {
        leaveRepository.save(leaveRequest);
    }

    /**
     * 提供教师查询自己申请请求的方法
     * @param id
     * @return
     */
    @Override
    public List<LeaveRequestDTO> getLeaveRequestById(String id) {
        List<LeaveRequestDTO> dtos = new ArrayList<>();
        for(LeaveRequest leaveRequest : leaveRepository.findByStaffId(id)){
            dtos.add(convertToDTO(leaveRequest));
        }
        return dtos;
    }

    @Override
    public String getStaffNameById(String staffId) {
        return staffRepository.findByStaffId(staffId).getName();
    }

}
