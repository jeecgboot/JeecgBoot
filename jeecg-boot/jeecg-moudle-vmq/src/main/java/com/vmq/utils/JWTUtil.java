package com.vmq.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 *
 */
public class JWTUtil {
	public static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

	/**
	 * 生产token
	 */
	public static String getToken(String username, String password) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(password);
		// 附带username信息
		return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
	}

	/**
	 * 验证token
	 */
	public static Boolean verify(String token, String username, String password) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(password);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getUsername(String token) {
		if (token == null) {
			return "";
		}
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

}