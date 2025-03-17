package com.changping.backend.controller;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.service.LeaveService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkLeave")
public class CheckLeaveController {
    private LeaveService leaveService;
    public CheckLeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/byname")
    public List<LeaveRequestDTO> getLeavesByName(@RequestParam String name) {    // @RequestParam 适合获取多个对象
        return leaveService.getLeaveRequestByName(name);
    }
}
