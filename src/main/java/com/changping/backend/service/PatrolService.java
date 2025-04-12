package com.changping.backend.service;

import com.changping.backend.entity.patrol;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatrolService {
    List<patrol> getTodayPatrolsByStaffId(String staffId);

    ResponseEntity<?> checkPatrol(String location, String staffId);
}
