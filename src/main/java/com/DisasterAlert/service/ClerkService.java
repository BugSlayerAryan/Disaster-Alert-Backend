package com.DisasterAlert.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ClerkService {

    private final WebClient webClient;

    // Replace with your Clerk API key from properties
    private final String clerkApiKey = "sk_test_ecF7zfXL1f4DnDUhrgqzWHsKxbhkHNvYYE4rPqZiAQ";

    public ClerkService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.clerk.dev/v1").build();
    }

    public Map<String, Object> verifyToken(String token) {
        return webClient.get()
                .uri("/sessions?token=" + token)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + clerkApiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
