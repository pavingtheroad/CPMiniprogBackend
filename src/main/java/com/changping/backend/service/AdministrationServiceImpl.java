package com.changping.backend.service;

import com.changping.backend.DTO.StaffDTO;
import com.changping.backend.entity.staff;
import com.changping.backend.repository.StaffRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministrationServiceImpl implements AdministrationService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    public AdministrationServiceImpl(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<StaffDTO> getStaffsInPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("permission"));
        Page<staff> staffPage = staffRepository.findAll(pageable);
        return staffPage.map(StaffDTO::fromStaff);
    }

    @Override
    public List<StaffDTO> getStaffsByName(String name) {
        List<staff> staffList = staffRepository.findAllByName(name);
        return staffList.stream()
                .map(StaffDTO::fromStaff)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> updatePassword(String staffId, String newPassword) {
        try {
            Optional<staff> staffOpt = Optional.ofNullable(staffRepository.findByStaffId(staffId));
            staff staff = null;
            if (staffOpt.isPresent()) {
                staff = staffOpt.get();
                staff.setPassword(passwordEncoder.encode(newPassword));
            }
            staffRepository.save(staff);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("出现异常" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> registerStaff(staff staff) {
        if (staffRepository.findByStaffId(staff.getStaffId()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "用户已存在"));
        }
        // **加密密码**
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        // **存入数据库**
        staffRepository.save(staff);

        return ResponseEntity.ok("注册成功");
    }

    @Override
    public ResponseEntity<?> deleteStaff(String staffId) {
        try {
            staffRepository.deleteByStaffId(staffId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "未找到删除用户"));
        }
    }
}
