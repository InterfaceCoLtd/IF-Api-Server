package xyz.interfacesejong.interfaceapi.global.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserSignResponse;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secretKey = "0123456789defaultSecretKey0123456789";
    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    private final Long VALIDITY_TIME = 60 * 60 * 1000L;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserSignResponse userSignResponse){

        Claims claims = Jwts.claims();
        claims.put("authLevel", userSignResponse.getAuthLevel().name());
        claims.put("email", userSignResponse.getEmail());
        claims.put("userId", userSignResponse.getId().toString());
        Date now = new Date();

        String token;
        if (userSignResponse.getDeviceId() != null){
            token = Jwts.builder()
                    .setClaims(claims)
                    .setAudience(userSignResponse.getDeviceId().toString())
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + VALIDITY_TIME))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }else {
            token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + VALIDITY_TIME))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }

        LOGGER.info("[generateToken] {}", token);
        return token;
    }

    public boolean isValidatedToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception){
            LOGGER.info("만료된 JWT 토큰");
            return false;
        } catch (IllegalArgumentException exception){
            LOGGER.info("잘못된 JWT 토큰");
            return false;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            LOGGER.info("잘못된 JWT 서명");
            return false;
        } catch (RuntimeException exception){
            LOGGER.info("valid token error");
            return false;
        }
    }

    public String resolveToken(HttpServletRequest httpServletRequest){
        return httpServletRequest.getHeader("Authorization");
    }

    public AuthLevelType getAuthLevel(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return AuthLevelType.valueOf(claims.getBody().get("authLevel").toString());
        } catch (Exception e){
            return null;
        }
    }
}