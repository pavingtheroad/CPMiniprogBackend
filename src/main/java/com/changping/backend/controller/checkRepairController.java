package com.changping.backend.controller;

import com.changping.backend.entity.repair;
import com.changping.backend.service.repair.RepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkRepair")
public class checkRepairController {
    private final RepairService repairService;
    public checkRepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/getRepair")
    public List<repair> checkRepair(@RequestParam String handled) {
        boolean flag = "true".equalsIgnoreCase(handled);    // 判断入参是否为true
        return repairService.getRepairListByHandled(flag);
    }

    @PostMapping("/setHandled")
    public ResponseEntity<?> setHandled(Integer id, Boolean handled) {
        return repairService.setHandled(id, handled);
    }
}
