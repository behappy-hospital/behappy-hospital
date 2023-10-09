package org.xiaowu.behappy.common.core.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * @author xiaowu
 */
public class JwtHelper {
    /**
     * 有效期
     */
    private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

    /**
     * 设置秘钥明文
     */
    private static final String TOKEN_SIGN_KEY = "dced2b9b0f4d4096a441bb4cb80e7271822fb46dd75447a98ba51379d4ffd0c4";

    public static String createToken(String userId, String userName) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) //设置唯一编号
                .setSubject("YYGH-USER") //设置主题  可以是JSON数据
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION)) //用于设置过期时间 ，参数为Date类型数据
                .claim("userId", userId) //设置userId
                .claim("userName", userName) //设置userName
                .signWith(generalKey()) //设置签名 使用HS256算法，并设置SecretKey(字符串)
                .compressWith(CompressionCodecs.GZIP) // 启用压缩
                .compact(); // 返回字符串
    }

    public static Long getUserId(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(generalKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong((String) claims.get("userId"));
    }

    public static String getUserName(String token) {
        if (StrUtil.isEmpty(token)) {
            return "undefined";
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(generalKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("userName");
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return SecretKey
     */
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(TOKEN_SIGN_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, SignatureAlgorithm.HS256.getJcaName());
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken("129", "55");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}
