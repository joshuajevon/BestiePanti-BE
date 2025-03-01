package com.app.bestiepanti.configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class GoogleTokenConfig {

    private final RestTemplate restTemplate;

    public GoogleTokenConfig() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Verifies a Google ID token using the tokeninfo endpoint.
     *
     * @param idTokenString The Google ID token to verify.
     * @return A Map containing the token payload if valid, or null if invalid.
     */
    public Map<String, Object> verify(String idTokenString) {
        try {
            if (idTokenString == null || idTokenString.isEmpty()) {
                log.error("Token verification failed: Token is null or empty.");
                return null;
            }

            // Send a GET request to the tokeninfo endpoint
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idTokenString;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Check if the response status is successful
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> payload = response.getBody();
                if (payload != null && payload.containsKey("email")) {
                    log.info("Token verification succeeded.");
                    return payload;
                }
            } else {
                log.error("Token verification failed: Invalid response from tokeninfo endpoint.");
            }
        } catch (Exception e) {
            log.error("Token verification failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
