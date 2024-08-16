package com.app.beatbong.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.jwt")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JWTServiceConfig {

    private String secret;

    private String expirationTime;

    public long getExpirationTime() {
        return Long.parseLong(expirationTime);
    }
}
