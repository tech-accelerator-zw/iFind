package com.techaccelarators.ifind.security;

import com.techaccelarators.ifind.exception.InvalidRequestException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    //generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime()+ jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }
    //get username from token
    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    //validate jwt token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch(SignatureException ex){
            throw new InvalidRequestException("Invalid JWT signature");
        }catch(MalformedJwtException ex){
            throw new InvalidRequestException("Invalid JWT token");
        }catch(ExpiredJwtException ex){
            throw new InvalidRequestException("Expired JWT token");
        }catch(UnsupportedJwtException ex){
            throw new InvalidRequestException("Unsupported JWT token");
        }catch(IllegalArgumentException ex){
            throw new InvalidRequestException("JWT claims string is empty");
        }

    }
}
