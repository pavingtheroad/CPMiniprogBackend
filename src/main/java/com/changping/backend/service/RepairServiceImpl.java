package com.changping.backend.service;

import com.changping.backend.entity.repair;
import com.changping.backend.repository.RepairRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {
    private final RepairRepository repairRepository;
    public RepairServiceImpl(RepairRepository repairRepository) {
        this.repairRepository = repairRepository;
    }

    @Override
    public List<repair> getRepairListByStaffId(String staffId) {
        return repairRepository.findByStaffId(staffId);
    }

    @Override
    public List<repair> getRepairListByHandled(Boolean handled) {
        return repairRepository.findByHandle(handled);
    }

    @Override
    public ResponseEntity<?> postRepairApply(repair repair) {
        repairRepository.save(repair);
        return ResponseEntity.ok().build();
    }

}
