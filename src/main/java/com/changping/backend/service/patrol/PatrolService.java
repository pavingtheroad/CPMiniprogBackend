package com.changping.backend.service.patrol;

import com.changping.backend.DTO.PatrolDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatrolService {
    /**
     * 获取教职工当天巡逻的地点列表
     * @param staffId
     * @return
     */
    List<PatrolDTO> getTodayPatrolsByStaffId(String staffId);

    ResponseEntity<?> checkPatrol(String location, String staffId);
}
