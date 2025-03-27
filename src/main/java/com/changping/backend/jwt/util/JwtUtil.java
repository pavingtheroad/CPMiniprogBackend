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
     * @param staffId
     * @param permission
     * @param secret
     * @return JWS串
     * @throws com.nimbusds.jose.JOSEException
     */
    public static String generateTokenByHMAC(String staffId, List<String> permission, String secret) throws JOSEException {
        try {
            // 创建 JWT 载荷
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(staffId)  // 设置 staffId
                    .claim("permission", new ArrayList<>(permission))  // 确保 List 正确存储
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))  // 1 小时后过期
                    .build();

            // 创建 JWS 头
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            // 创建 JWT 并签名
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            JWSSigner jwsSigner = new MACSigner(secret);
            signedJWT.sign(jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new JOSEException("生成 Token 失败: " + e.getMessage(), e);
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
            // 解析 JWT
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            // 验证签名
            if (!signedJWT.verify(verifier)) {
                return null;  // 签名无效
            }

            // 获取 JWT 载荷
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // 检查 Token 是否过期
            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                return null;  // Token 已过期
            }

            // 解析 staffId
            String staffId = claimsSet.getSubject();

            // 解析权限信息
            List<String> userPermission = claimsSet.getStringListClaim("permission");
            if (userPermission == null || userPermission.isEmpty()) {
                userPermission = Collections.singletonList("user");  // 默认权限
            }

            // 构造用户数据
            Map<String, Object> userData = new HashMap<>();
            userData.put("staffId", staffId);
            userData.put("permission", userPermission);

            return userData;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }
}
