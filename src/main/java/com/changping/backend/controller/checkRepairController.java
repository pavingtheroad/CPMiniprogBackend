package com.changping.backend.controller;

import com.changping.backend.entity.repair;
import com.changping.backend.service.RepairService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkRepair")
public class checkRepairController {
    private RepairService repairService;
    public checkRepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping
    public List<repair> checkRepair(Boolean handled) {
        return repairService.getRepairListByHandled(handled);
    }
}
