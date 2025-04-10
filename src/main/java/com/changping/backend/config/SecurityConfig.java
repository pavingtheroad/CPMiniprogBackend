package com.changping.backend.config;

import com.changping.backend.jwt.filter.JwtAuthenticationFilter;
import com.changping.backend.service.StaffUserDetailService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @PostConstruct
    public void init() {
        System.out.println("SecurityConfig initialized");
    }
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final StaffUserDetailService staffUserDetailService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, StaffUserDetailService staffUserDetailService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.staffUserDetailService = staffUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("securityFilterChain");
        http
                .csrf(csrf -> csrf.disable())  // 关闭CSRF，适用于API接口
                .authorizeHttpRequests(authz ->
                        authz.requestMatchers("/login", "/register").permitAll()  // 跳过login和register接口
                                .requestMatchers("/uploadimage/**").permitAll()
                                .requestMatchers("/uploadRepair/**").permitAll()
                                .requestMatchers("/feedback/**").authenticated()    // 虽然允许全部用户访问，但用户必须登录
                                .requestMatchers("/checkFeedback/**").hasAnyAuthority("ROLE_admin")
                                .requestMatchers("/leave/**").hasAnyAuthority("ROLE_teacher", "ROLE_admin")
                                .requestMatchers("/checkLeave").hasAnyAuthority("ROLE_teacher", "ROLE_admin", "ROLE_guard")
                                .requestMatchers("/repair/**").authenticated()
                                .requestMatchers("checkRepair/**").hasAnyAuthority( "ROLE_admin", "ROLE_repairman")
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        System.out.println("authenticationManager bean is being created");
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(staffUserDetailService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        System.out.println("authenticationManager bean created successfully");
        return authenticationManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 密码加密器
    }
}