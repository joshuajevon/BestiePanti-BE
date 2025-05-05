package com.app.bestiepanti.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class JwtConfig {
    
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    private Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private Map<String, String> activeUserTokens = new HashMap<>();

    public String getActiveToken(String username) {
        return activeUserTokens.get(username);
    }

    public void storeActiveToken(String username, String token) {
        activeUserTokens.put(username, token);
    }
}
