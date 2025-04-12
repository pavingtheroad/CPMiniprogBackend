package com.changping.backend.controller;

import com.changping.backend.entity.patrol;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.service.PatrolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patrol")
public class PatrolController {
    private final PatrolService patrolService;
    public PatrolController(PatrolService patrolService) {
        this.patrolService = patrolService;
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String checkLocation
    ){
        try{
            String token = authorizationHeader.replace("Bearer ", "");
            Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
            String staffId = (String) claims.get("staffId");
            return patrolService.checkPatrol(checkLocation, staffId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/getMyCheckins")
    public List<patrol> getMyCheckins(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        return patrolService.getTodayPatrolsByStaffId(staffId);
    }
}
