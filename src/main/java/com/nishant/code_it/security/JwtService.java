package com.nishant.code_it.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	
	public String createToken(String username , Role role)
	{
		SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
		String jwt = Jwts.builder()
				         .claim("username", username)
				         .claim("authorities", role)
				         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				         .signWith(key)
				         .compact();
		
		return jwt;
	}
  
	public void logout(String jwt)
	{
		SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
		Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		claim.setExpiration(new Date(System.currentTimeMillis()));
	}
}
