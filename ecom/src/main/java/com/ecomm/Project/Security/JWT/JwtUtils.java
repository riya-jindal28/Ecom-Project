package com.ecomm.Project.Security.JWT;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ecomm.Project.Security.Services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
    
        private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.JwtExpiration}")
    private int JwtExpiration;

     @Value("${spring.app.JwtSecret}")
    private String JwtSecret;

    @Value("${spring.app.JwtCookie}")
    private String jwtCookie;

    //Getting JWT from Header
    // public String getJWTFronHeader(HttpServletRequest request){
    //     String bearerToken = request.getHeader("Authorization");
    //     logger.debug("Authorization Header: {}", bearerToken);
    //     if(bearerToken != null && bearerToken.startsWith("Bearer ")){
    //         return bearerToken.substring(7);
    //     }
    //     return null;
    // }
    
    //Generating Token from Username
    public String generateTokenFromUsername(String username){
        return Jwts.builder()
              .subject(username)
              .issuedAt(new Date())
              .expiration(new Date((new Date().getTime() + JwtExpiration)))
              .signWith(key())
              .compact();
    }
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
        .path("/api")
        .maxAge(24*60*60)
        .httpOnly(false)
        .build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
        .path("/api")
        .build();
        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }
        else{
            return null;
        }
    }

     //Validate JWT token
   public boolean validateJwtToken(String authToken){
        try{
            System.out.println("Validate");
            Jwts.parser()
               .verifyWith((SecretKey) key())
               .build()
               .parseSignedClaims(authToken);
            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    //Getting Username from Token
    public String getUserNameFromJWTToken(String token){
        return Jwts.parser()
               .verifyWith((SecretKey) key())
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .getSubject();
    }

    //Generating Signing Key
    public Key key(){
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(JwtSecret)
        );
    }

}
