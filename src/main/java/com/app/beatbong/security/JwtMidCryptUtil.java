package com.app.beatbong.security;

import com.app.beatbong.config.JWTServiceConfig;
import com.app.beatbong.model.user.AppUser;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class JwtMidCryptUtil {
    public static void main(String[] args) {
        Integer userId = 123;
        String encryptedUserId = MidCrypt.encrypt(String.valueOf(userId));
        JWTServiceConfig config = new JWTServiceConfig();
        config.setSecret("12345jhakdjfjkcjkhfdjkvlsfhblhvbalsdfblvbajksdbfjkbvjkdjsglfgaflsdhgljhcvljSHDvljcvlSJDHvljvc");
        JwtTokenizer tokenizer = new JwtTokenizer(config);
        JWTPayload payload = new JWTPayload();
        payload.setFirstName("laxman");
        payload.setLastName("maske");
        payload.setRole("admin");
        payload.setUsername("laxman");
        String token = tokenizer.generateAuthonticatedToken(payload);
        log.info("Encrypted userId: {}", encryptedUserId);
        log.info("Token: {}", token);
    }
}
