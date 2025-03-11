package com.changping.backend.controller;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;
import com.changping.backend.service.LeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/byname")
    public List<LeaveRequestDTO> getLeavesByName(@RequestParam String name) {    // @RequestParam 适合获取多个对象
        return leaveService.getLeaveRequestByName(name);
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitLeave(@RequestBody LeaveRequest leaveRequest) {
        System.out.println("submitLeave");
        leaveService.postLeaveRequest(leaveRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byStaffid")
    public List<LeaveRequestDTO> getLeavesByStaffId(@RequestParam String staffId) {
        System.out.println("getLeavesByStaffId");
        if (staffId == null || staffId.isEmpty()) {
            return null;
        }
        else return leaveService.getLeaveRequestById(Integer.parseInt(staffId));
    }
}
