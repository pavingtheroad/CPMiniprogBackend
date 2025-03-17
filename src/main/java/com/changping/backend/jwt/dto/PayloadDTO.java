package com.changping.backend.jwt.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PayloadDTO {
    // 主题
    private String sub;
    // 签发时间
    private Long iat;
    //过期时间
    private Long exp;
    // JWT's ID
    private String jti;
    // 员工名称
    private String username;
    // 用户权限
    private List<String> authorities;
}
