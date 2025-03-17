package com.changping.backend.config.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.changping.backend.jwt.dto.PayloadDTO;
import com.changping.backend.jwt.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<String> authoritiesList = new ArrayList<>(authorities.size());
            authorities.forEach(authority -> authoritiesList.add(authority.getAuthority()));

            Date now = new Date();
            Date expiryDate = DateUtil.offsetSecond(now, 60 * 60);    // token有效期 1hour
            PayloadDTO payloadDTO = PayloadDTO.builder()
                    .sub(userDetails.getUsername())
                    .iat(now.getTime())
                    .exp(expiryDate.getTime())
                    .jti(UUID.randomUUID().toString())
                    .username(userDetails.getUsername())
                    .authorities(authoritiesList)
                    .build();
            String token = null;
            try{
                token = JwtUtil.generateTokenByHMAC(
                        JSONUtil.toJsonStr(payloadDTO),    // nimbus-jose-jwt所使用的HMAC，SHA256算法
                        SecureUtil.md5(JwtUtil.DEFAULT_SECRET)    // 所需密匙长度至少要32个字节，因此先用md5加密
                );
                response.setHeader("Authorization", token);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write("登录成功");
                out.close();
            }catch(JOSEException e){
                e.printStackTrace();
            }
        }
    }
}
