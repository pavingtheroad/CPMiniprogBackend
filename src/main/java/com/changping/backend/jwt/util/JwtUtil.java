package com.changping.backend.jwt.util;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.*;

public class JwtUtil {
//     默认密匙
    public static final String DEFAULT_SECRET = "mySuperSecretKey1234567890123456";

    /**
     * 使用HMAC SHA-256,生成签名
     * @param userName
     * @param permission
     * @param secret
     * @return JWS串
     * @throws com.nimbusds.jose.JOSEException
     */
    public static String generateTokenByHMAC(String userName, List<String> permission, String secret) throws JOSEException {
        try {//创建JWS头，设置签名算法和类型
            System.out.println("generateTokenByHMAC" + "生成 JWT 时的权限：" + permission);
            // 创建 JWT 载荷
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userName)  // 相当于 "sub": userName
                    .claim("permission", permission)  // 存储角色权限
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))  // 1小时后过期
                    .build();
            // JWS 头
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();
            // 创建Jwt对象
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);

            JWSSigner jwsSigner = new MACSigner(secret);        // 创建HMAC签名器

            signedJWT.sign(jwsSigner);        // 签名

            return signedJWT.serialize();
        }catch (JOSEException e) {
            e.printStackTrace();
            throw new JOSEException("Error while generating token: " + e.getMessage(), e);
        }
    }
    /**
     * 验证JWT token的有效性，并返回解析后的用户信息
     * @param token  JWT Token
     * @param secret 密钥
     * @return 包含用户信息的Map 或 null（验证失败）
     */
    public static Map<String, Object> verifyToken(String token, String secret) {
        try {
            System.out.println("verifyToken");
            // 解析JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 创建签名验证器
            JWSVerifier verifier = new MACVerifier(secret);

            // 验证签名
            if (!signedJWT.verify(verifier)) {
                System.out.println("JWT verification failed");
                return null;  // 签名无效
            }

            // 获取载荷
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // 检查是否过期
            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                System.out.println("JWT expiration date is before now");
                return null;  // Token 已过期
            }

            System.out.println("JWT Claims: " + claimsSet.toJSONObject());
            String userName = claimsSet.getSubject();
            List<String> userPermission = claimsSet.getStringListClaim("permission");    // 解析 List
            // 防止用户 permission 在数据库中为 null 导致的 NullPointerException
            if (userPermission == null) {
                userPermission = new ArrayList<>();
                userPermission.add("user"); // 给予默认权限
            }
            System.out.println("解析 JWT：userName=" + userName + ", userPermissions=" + userPermission);
            // 提取用户信息
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", userName);  // 获取存储的用户名
            userData.put("permission", userPermission);  // 获取角色,提取permission字段

            return userData;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();  // 记录异常信息
            System.out.println("出现异常");
            return null;
        }
    }
}
