package com.changping.backend.service.repair;

import com.changping.backend.entity.repair;
import com.changping.backend.repository.RepairRepository;
import com.changping.backend.repository.StaffRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {
    private final StaffRepository staffRepository;
    private final RepairRepository repairRepository;
    public RepairServiceImpl(RepairRepository repairRepository, StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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
        String name = staffRepository.findByStaffId(repair.getStaffId()).getName();
        repair.setName(name);
        repairRepository.save(repair);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> setHandled(Integer id, Boolean handled) {
        try{
            repair repair = repairRepository.findById(id).get();
            repair.setHandle(handled);
            repairRepository.save(repair);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            // 捕获任何异常并返回 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update repair record: " + e.getMessage());
        }

    }


}
