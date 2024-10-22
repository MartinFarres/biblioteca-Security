package com.is.biblioteca.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  public String getToken(UserDetails user){
    return getToken(new HashMap<>(), user);
  }

  private String getToken(Map<String,Object> extraClaims, UserDetails user) {
    return Jwts
      .builder()
      .setClaims(extraClaims)
      .setSubject(user.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
      .signWith(getKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key getKey() {
    byte[] keyBites=Decoders.BASE64.decode(System.getenv("SECRET_KEY"));
    return Keys.hmacShaKeyFor(keyBites);
  
  }

  public String getUsernameFromToken(String token) {
    return getClaims(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }


  private Claims getAllClaims(String token){
    return Jwts
      .parserBuilder()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public <T> T getClaims(String token, Function<Claims, T> claimsResolver){
    final Claims claims=getAllClaims(token);
    return claimsResolver.apply(claims);
  }
  
  private Date getExpiration(String token){
    return getClaims(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token){
    return getExpiration(token).before(new Date());
  }

}
