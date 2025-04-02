package com.changping.backend.controller;

import com.changping.backend.entity.staff;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.result.JwtResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody staff staff) throws JOSEException {
        try {
            // 用 name 进行身份验证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(staff.getName(), staff.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                staff loggedInStaff = staffRepository.findByName(userDetails.getUsername()); // 获取完整的用户信息

                if (loggedInStaff == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户不存在");
                }

                // 这里获取 staffId，而不是 name
                String staffId = loggedInStaff.getStaffId();

                // 获取权限信息
                List<String> permissions = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                // 生成 JWT，sub 存 staffId
                String jwt = JwtUtil.generateTokenByHMAC(staffId, permissions, JwtUtil.DEFAULT_SECRET);

                return ResponseEntity.ok(new JwtResponse(jwt));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("认证失败");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("认证失败");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody staff staff) {
        // **修正 `findByName` 逻辑**
        if (staffRepository.findByName(staff.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户已存在");
        }

        // **加密密码**
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));

        // **存入数据库**
        staffRepository.save(staff);

        return ResponseEntity.ok("注册成功");
    }

}
