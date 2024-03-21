package com.yjc.platform.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;

import java.util.Date;

public class JWTUtil {
    public static String sign(Long id,String info,long expire,String secret){
        return JWT.create()
                .withAudience(id.toString())
                .withClaim("info",info)
                .withExpiresAt(new Date(System.currentTimeMillis()+expire*1000))
                .sign(Algorithm.HMAC256(secret));

    }
    public static boolean search(String token,String secret){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        verifier.verify(token);
        return true;
    }
    public static String getInfo(String token){
        try {
            return JWT.decode(token).getClaim("info").asString();
        }
        catch (JWTDecodeException e){
            return null;
        }
    }
    public static Long getId(String token){
        try {
            String id = JWT.decode(token).getAudience().get(0);
            return Long.parseLong(id);
        }
        catch (JWTDecodeException e){
            return null;
        }
    }


}
