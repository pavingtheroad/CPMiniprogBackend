package com.changping.backend.controller;

import com.changping.backend.entity.staff;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.result.JwtResponse;
import com.changping.backend.service.StaffUserDetailService;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        System.out.println("login");
        // 使用Spring Security的认证管理器进行身份验证
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(staff.getName(), staff.getPassword())
            );
            System.out.println("Authentication object: " + authentication); // 打印认证对象
            if (authentication.isAuthenticated()) {
                System.out.println("Authentication successful");
            } else {
                System.out.println("Authentication failed");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 如果认证通过，生成JWT Token
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String staffName = authentication.getName();
            String permission = staff.getPermission();
            // payload存储职工姓名和权限
            String jwt = JwtUtil.generateTokenByHMAC(staffName + "|" + permission,JwtUtil.DEFAULT_SECRET);
            // 返回JWT Token
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed with exception: " + e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("认证失败");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody staff staff) {
        System.out.println("register");

        // 检查用户是否已存在
        if (staffRepository.findByName(staff.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户已存在");
        }

        // **手动加密密码后存入数据库**
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        staff.setPassword(encoder.encode(staff.getPassword()));

        // 存入数据库
        staffRepository.save(staff);

        return ResponseEntity.ok("注册成功");
    }

}
