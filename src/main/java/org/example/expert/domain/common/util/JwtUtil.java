package org.example.expert.domain.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	private final Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Long userId, String email, UserRole userRole) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.claim("userRole", userRole)
				.setExpiration(new Date(date.getTime() + TOKEN_TIME))
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		throw new RuntimeException("Not Found Token");
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
