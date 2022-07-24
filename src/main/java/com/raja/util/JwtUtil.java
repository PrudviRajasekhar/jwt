package com.raja.util;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${app.secret}")
	private String secret;
	
	//6. validate username in token and database and expire date
	public boolean validateToken(String token, String username) {
		String tokenUserName = getUsername(token);
		return (username.equals(tokenUserName) && !isTokenExpired(token));
	}
	
	//5. Validate Expired Date
	public boolean isTokenExpired(String token) {
		Date expdate = getExpdate(token);
		return expdate.before(new Date(System.currentTimeMillis()));
	}
	
	//4. Read Subject (USERNAME)
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}
	
	//3. Read Expire Date
	public Date getExpdate(String token) {
		return getClaims(token).getExpiration();
	}
	
	//2. Read Claims
	public Claims getClaims(String token) {
		
		return Jwts.parser()
				.setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
				.parseClaimsJws(token)
				.getBody()
				;
	}
	
	//1. Generate Token
	public String generateToken(
			String subject)
	{
		
		return Jwts.builder()
				.setSubject(subject)
				.setIssuer("HEAC")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secret.getBytes()))
				.compact()
				;
	}
}
