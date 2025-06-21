package com.changping.backend.controller;

import com.changping.backend.DTO.StaffDTO;
import com.changping.backend.entity.staff;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.service.AdministrationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdministrationController {
    private final AdministrationService administrationService;
    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;

    }

    @GetMapping("/usersByName")
    public ResponseEntity<?> findStaffByName(@RequestParam String name) {
        try {
            List<StaffDTO> staffs = administrationService.getStaffsByName(name);
            return ResponseEntity.ok().body(staffs);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userspage")
    public ResponseEntity<?> findStaffByPage(@RequestParam(defaultValue = "0    ") int page, @RequestParam(defaultValue = "10") int size) {
        Page<StaffDTO> staffPage = administrationService.getStaffsInPage(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("staffList",staffPage.getContent());
        response.put("totalPages",staffPage.getTotalPages());
        response.put("totalElements",staffPage.getTotalElements());
        response.put("currentPage",staffPage.getNumber());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestParam String staffId, @RequestParam(defaultValue = "123456") String newPassword) {
        return administrationService.updatePassword(staffId, newPassword);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody staff staff) {
        return administrationService.registerStaff(staff);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteStaff(@RequestParam String staffId) {
        return administrationService.deleteStaff(staffId);
    }
}
