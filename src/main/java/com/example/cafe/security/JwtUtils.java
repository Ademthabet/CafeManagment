package com.example.cafe.security;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {
	private String secret= "cafe";
	 private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateToken(String username,String role) {
		Map<String,String> cliams = new HashMap<>();
		cliams.put("role", role);
		return createToken(cliams,username);
	}
	private String createToken(Map<String,String> claims, String subject) {
		return  Jwts.builder()
                .setClaims(claims) // Ajout des claims
                .setSubject(subject) // Définir le sujet (nom d'utilisateur)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Date d'émission
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration 10 heures
                .signWith(key, SignatureAlgorithm.HS256) // Signature avec la clé secrète et l'algorithme
                .compact();
	}
}
