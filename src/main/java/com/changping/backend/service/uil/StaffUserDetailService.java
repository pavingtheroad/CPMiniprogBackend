package com.changping.backend.service.uil;

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
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {
        staff staff = staffRepository.findByStaffId(staffId);  // 按 name 查找

        if (staff == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(staff.getPermission().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                staff.getName(),  // 这里传 name
                staff.getPassword(),
                authorities
        );
    }

}
