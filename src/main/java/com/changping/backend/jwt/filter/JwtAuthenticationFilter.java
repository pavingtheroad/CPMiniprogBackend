package com.changping.backend.jwt.filter;

import com.changping.backend.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal");
        // 如果是/login或/register请求，跳过JWT验证
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register")) {
            filterChain.doFilter(request, response);  // 放行
            return;
        }

        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                // 解析Token，获取用户ID和角色信息
                Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
                if (claims != null) {
                    String staffId = (String) claims.get("staffId");
                    List<String> permissions = (List<String>) claims.get("permission");
//                    System.out.println("Filter中permission："+ permissions + "username" + username);
                    if (staffId != null && permissions != null) {
                        // 创建权限列表
                        List<GrantedAuthority> authorities = permissions.stream()
                                .map(SimpleGrantedAuthority::new)  // 转换为 Spring Security 的权限对象
                                .collect(Collectors.toList());

                        // 创建 Spring Security 认证对象
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                staffId, null, authorities);  // 使用用户名和权限创建认证对象

                        // 将认证对象存入 SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        if (authentication != null) {
                            System.out.println("当前用户权限：" + authentication.getAuthorities());
                        } else {
                            System.out.println("Authentication is null.");
                        }
                    }
                } else {
                    // Token无效，返回401
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("非法或过期的token");
                    return;
                }
            } catch (Exception e) {
                // 发生异常，返回401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token认证失败");
                return;
            }
        }
        filterChain.doFilter(request, response);  // 放行请求
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
