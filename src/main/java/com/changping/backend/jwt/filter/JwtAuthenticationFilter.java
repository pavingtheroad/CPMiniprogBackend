package com.changping.backend.jwt.filter;

import cn.hutool.crypto.SecureUtil;
import com.changping.backend.jwt.dto.PayloadDTO;
import com.changping.backend.jwt.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                // 验证Token并获取用户ID
                String userId = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);

                if (userId != null) {
                    // 如果Token验证成功，则设置认证信息
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
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
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

//    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws ParseException, JOSEException, java.text.ParseException {
//        PayloadDTO payloadDTO = JwtUtil.verifyTokenByHMAC(token, SecureUtil.md5(DEFAULT_SECRET));
//        String username = payloadDTO.getUsername();
//        List<String> permission = payloadDTO.getAuthorities();
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (String authority : permission) {
//            authorities.add(new SimpleGrantedAuthority(authority));
//        }
//        if (username!=null){
//            return new UsernamePasswordAuthenticationToken(username, null, authorities);
//        }
//        return null;
//    }
}
