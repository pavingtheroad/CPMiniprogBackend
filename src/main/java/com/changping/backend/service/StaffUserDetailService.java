package com.changping.backend.service;

import com.changping.backend.entity.staff;
import com.changping.backend.repository.StaffRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffUserDetailService implements UserDetailsService {
    private final StaffRepository staffRepository;

    public StaffUserDetailService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        // 从数据库查询用户
        staff staff = staffRepository.findByName(username);

        // 如果用户不存在
        if (staff == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        // 添加 ROLE_ 前缀
        String permission = staff.getPermission();
        // **转换为 List<String>**
        List<SimpleGrantedAuthority> authorities = Arrays.stream(permission.split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim())) // 确保 "ROLE_" 前缀
                .collect(Collectors.toList());
        // 返回加密后的密码
        return new org.springframework.security.core.userdetails.User(
                staff.getName(),
                staff.getPassword(),
                authorities
        );
    }
}
