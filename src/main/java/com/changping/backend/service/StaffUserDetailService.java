package com.changping.backend.service;

import com.changping.backend.entity.staff;
import com.changping.backend.repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffUserDetailService implements UserDetailsService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffUserDetailService(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户
        staff staff = staffRepository.findByName(username);

        // 如果用户不存在
        if (staff == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        // 返回加密后的密码
        return User.withUsername(staff.getName())
                .password(staff.getPassword())  // 假设数据库中的密码已经加密
                .roles(staff.getPermission())
                .build();
    }
}
