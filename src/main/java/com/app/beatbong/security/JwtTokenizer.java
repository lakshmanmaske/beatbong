package com.app.beatbong.security;

import com.app.beatbong.config.JWTServiceConfig;
import com.app.beatbong.exception.BeatBongErrorEnum;
import com.app.beatbong.exception.BeatBongServiceException;
import com.app.beatbong.model.user.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JwtTokenizer {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JWTServiceConfig jwtServiceConfig;

    @Autowired
    public JwtTokenizer(JWTServiceConfig jwtServiceConfig) {
        this.jwtServiceConfig = jwtServiceConfig;
    }

    public String generateAuthonticatedToken(JWTPayload jwtPayload) {
        return TOKEN_PREFIX +
                Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("role", jwtPayload.getRole())
                .claim("username", jwtPayload.getUsername())
                .claim("firstname", jwtPayload.getFirstName())
                .claim("lastname", jwtPayload.getLastName())
                .setExpiration(new Date(System.currentTimeMillis() + jwtServiceConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, jwtServiceConfig.getSecret())
                .compact();
    }

    public String refreshTokenIfExpiring(String token) throws ExpiredJwtException, UnsupportedJwtException,MalformedJwtException,
            SignatureException,IllegalArgumentException{
        JWTPayload jwtPayload = extractTokenInfo(token);
        return TOKEN_PREFIX + Jwts.builder()
                .setHeaderParam("type","JWT")
                .claim("role", jwtPayload.getRole())
                .claim("username", jwtPayload.getUsername())
                .claim("firstname", jwtPayload.getFirstName())
                .claim("lastname", jwtPayload.getLastName())
                .setExpiration(new Date(System.currentTimeMillis() + jwtServiceConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, jwtServiceConfig.getSecret())
                .compact();
    }

    public String resolveToken(String bearerToken){
        if(bearerToken==null){
            throw new BeatBongServiceException("JWT token is null !", BeatBongErrorEnum.UNAUTHORIZED);
        } else {
            boolean startsWith = bearerToken.startsWith(TOKEN_PREFIX);
            if(startsWith)
                return bearerToken.substring(7);
            else throw new BeatBongServiceException("Token does not start with Bearer !", BeatBongErrorEnum.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String token) throws CustomJwtException{
        try{
            final String message = Jwts.parser().setSigningKey(jwtServiceConfig.getSecret())
                    .parseClaimsJws(resolveToken(token))
                    .getBody()
                    .toString();
            return true;
        } catch (JwtException | IllegalArgumentException e){
            log.info(e.getMessage());
            throw new CustomJwtException("Expired or invalid JWT token");
        }
    }

    public JWTPayload extractTokenInfo(String token){
        JWTPayload jwtPayload = new JWTPayload();
        if(token != null){
            Map<String,Object> user2 = (Map<String, Object>) Jwts.parser()
                    .setSigningKey(jwtServiceConfig.getSecret())
                    .parseClaimsJws(resolveToken(token)).getBody();

            jwtPayload.setRole(user2.get("role").toString());
            jwtPayload.setUsername(user2.get("username").toString());
            jwtPayload.setFirstName(user2.get("firstname").toString());
            jwtPayload.setLastName(user2.get("lastname").toString());
            //jwtPayload.setUser(new ObjectMapper().convertValue(user2.get("user"), AppUser.class));
        }
        return jwtPayload;
    }
}
