package com.xiio.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

//jwt工具类
public class JwtHelper {

    //有效时长
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    //签名加密秘钥
    private static String tokenSignKey = "123456";

    //根据用户id和用户名称生成一个token字符串
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                //分类
                .setSubject("AUTH-USER")
                //设置token有效时长
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                //设置主题部分
                .claim("userId", userId)
                .claim("username", username)
                //签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                //对生产字符串进行压缩
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //从生成的token为字符串中获取用户id
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;
            //对加密编码进行秘钥解码
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            //得到密文主题部分
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //从生成token字符串中获取用户名称
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);

            Claims claims = claimsJws.getBody();

            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "admin");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUsername(token));
    }
}