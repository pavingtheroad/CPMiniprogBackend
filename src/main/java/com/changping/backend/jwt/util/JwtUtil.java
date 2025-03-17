package com.changping.backend.jwt.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.changping.backend.jwt.dto.PayloadDTO;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtil {
//     默认密匙
    public static final String DEFAULT_SECRET = "123456";
//    private static final String HMAC_SECRET = SecureUtil.md5(DEFAULT_SECRET); // 经过 MD5 加密的密钥
//
    /**
     * 使用HMAC SHA-256,生成签名
     * @param payloadStr
     * @param secret
     * @return JWS串
     * @throws com.nimbusds.jose.JOSEException
     */
    public static String generateTokenByHMAC(String payloadStr, String secret) throws JOSEException {
        try {//创建JWS头，设置签名算法和类型
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

            Payload payload = new Payload(payloadStr);        // 将载荷信息封装进Payload

            JWSSigner jwsSigner = new MACSigner(secret);        // 创建HMAC签名器

            JWSObject jwsObject = new JWSObject(jwsHeader, payload);        // 创建Jws对象

            jwsObject.sign(jwsSigner);        // 签名
            return jwsObject.serialize();
        }catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 验证JWT token的有效性
     * @param token JWT Token
     * @param secret 密钥
     * @return 用户标识或null（如果验证失败）
     * @throws JOSEException
     * @throws ParseException
     */
    public static String verifyToken(String token, String secret) throws ParseException, JOSEException {
        // 解析JWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 创建签名验证器，使用相同的密钥
        JWSVerifier verifier = new MACVerifier(secret);

        // 验证签名
        if (signedJWT.verify(verifier)) {
            // 返回载荷中存储的用户ID或其他标识
            return signedJWT.getJWTClaimsSet().getSubject();  // 你可以根据需要修改为其他字段
        } else {
            return null;  // 签名无效
        }
    }
//    /**
//     * 验证签名，提取有效载荷以payload对象形式返回
//     * @param token JWS串
//     * @param secret
//     * @return PayloadDTO
//     * @throws java.text.ParseException
//     * @throws JOSEException
//     */
//    public static PayloadDTO verifyTokenByHMAC(String token, String secret) throws JOSEException, ParseException {
//        JWSObject jwsObject = JWSObject.parse(token);
//        JWSVerifier verifier = new MACVerifier(secret);
//        if (!jwsObject.verify(verifier)) {
//            throw new JOSEException("Invalid JWT token");
//        }
//        String payloadStr = jwsObject.getPayload().toString();
//        PayloadDTO payloadDTO = JSONUtil.toBean(payloadStr, PayloadDTO.class);
//        if(payloadDTO.getExp() < new Date().getTime())
//            throw new JOSEException("token已过期");
//        return payloadDTO;
//    }
//    /**
//     * 解析token,获取Authentication
//     * @param token JWS串
//     * @return Authentication
//     */
//    public static Authentication getAuthentication(String token) throws JOSEException {
//        // 解析 JWT
//        try{
//            // 解析 Token
//            SignedJWT signedJWT = SignedJWT.parse(token);
//
//            // 验证 Token 签名
//            JWSVerifier verifier = new MACVerifier(HMAC_SECRET);
//            if (!signedJWT.verify(verifier)) {
//                return null; // 验证失败
//            }
//
//            // 获取 JWT 负载
//            String payload = signedJWT.getPayload().toString();
//            System.out.println("Token Payload: " + payload); // 调试用，可删除
//
//            // 从 JSON 负载中提取用户名（假设是 userName 字段）
//            String username = JSONUtil.parseObj(payload).getStr("name");
//
//            if (username != null) {
//                // 你可以根据实际情况解析权限信息
//                List<String> roles = JSONUtil.parseObj(payload).getJSONArray("permission").toList(String.class);
//                List<GrantedAuthority> authorities = roles.stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//                return new UsernamePasswordAuthenticationToken(username, null, authorities);
//            }
//        }catch (java.text.ParseException | com.nimbusds.jose.JOSEException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
