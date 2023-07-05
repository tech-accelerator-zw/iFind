package com.techaccelarators.ifind.security;

import com.techaccelarators.ifind.domain.UserAccount;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.repository.UserAccountRepository;
import com.techaccelarators.ifind.service.impl.OtpGenerator;
import com.techaccelarators.ifind.service.impl.OtpService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private OtpService otpService;
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    public String createToken(Authentication authentication){
        String username = authentication.getName();
        UserAccount user = userAccountRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found!"));
        if (user.getIsOtpRequired()) {
            otpService.generateOtp(user.getUsername());
            return "Check Your Email For The OTP";
        }
        return generateToken(authentication);
    }
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
    public String createTokenAfterVerifiedOtp(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return generateToken(authentication);
    }
    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
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
