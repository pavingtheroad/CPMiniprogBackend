package com.changping.backend.controller;

import com.changping.backend.entity.staff;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.result.JwtResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    public AuthController(AuthenticationManager authenticationManager, StaffRepository staffRepository) {
        this.authenticationManager = authenticationManager;
        this.staffRepository = staffRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody staff staff) throws JOSEException {
        // 使用Spring Security的认证管理器进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        staff.getName(),
                        staff.getPassword()
                )
        );
        // 如果认证通过，生成JWT Token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String staffName = authentication.getName();
        String permission = staff.getPermission();
        // payload存储职工姓名和权限
        String jwt = JwtUtil.generateTokenByHMAC(staffName + "|" + permission,JwtUtil.DEFAULT_SECRET);
        // 返回JWT Token
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
