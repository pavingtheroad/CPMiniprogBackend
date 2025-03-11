package com.changping.backend.service;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;
import com.changping.backend.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService{

    private LeaveRepository leaveRepository;
    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setName(leaveRequest.getName());
        dto.setClassNum(leaveRequest.getClass_num());
        dto.setStudentId(leaveRequest.getStudent_id());
        dto.setLeaveType(leaveRequest.getLeave_type());
        dto.setLeaveDate(leaveRequest.getLeave_date());
        dto.setRemarks(leaveRequest.getRemarks());
        return dto;
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestByName(String name) {
        List<LeaveRequestDTO> dtos = new ArrayList<>();
        for(LeaveRequest leaveRequest : leaveRepository.findByName(name)){
            dtos.add(convertToDTO(leaveRequest));
        }
        return dtos;
    }

    @Override
    public void postLeaveRequest(LeaveRequest leaveRequest) {
        leaveRepository.save(leaveRequest);
    }


}
