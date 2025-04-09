package com.changping.backend.controller;

import com.changping.backend.entity.repair;
import com.changping.backend.service.RepairService;
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
    public List<repair> checkRepair(Boolean handled) {
        return repairService.getRepairListByHandled(handled);
    }

    @PostMapping("/setHandled")
    public ResponseEntity<?> setHandled(Integer id, Boolean handled) {
        return repairService.setHandled(id, handled);
    }
}
