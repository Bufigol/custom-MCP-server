package com.claude.mcp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class NetworkServiceTest {
    
    private NetworkService networkService;
    
    @BeforeEach
    void setUp() {
        networkService = new NetworkService();
    }
    
    @Test
    void testValidHttpRequest() throws IOException, InterruptedException {
        String url = "https://api.github.com";
        String response = networkService.performHttpRequest(url, "GET", null, null, null);
        assertNotNull(response);
        assertTrue(response.contains("current_user_url"));
    }
    
    @Test
    void testInvalidUrl() {
        String url = "not a valid url";
        assertThrows(IllegalArgumentException.class, () -> {
            networkService.performHttpRequest(url, "GET", null, null, null);
        });
    }
    
    @Test
    void testBlockedDomain() {
        String url = "http://localhost";
        assertThrows(IllegalArgumentException.class, () -> {
            networkService.performHttpRequest(url, "GET", null, null, null);
        });
    }
    
    @Test
    void testUrlAccessibility() throws IOException {
        assertTrue(networkService.isUrlAccessible("https://api.github.com"));
        assertFalse(networkService.isUrlAccessible("https://this-is-not-a-real-domain.com"));
    }
    
    @Test
    void testHttpRequestWithHeaders() throws IOException, InterruptedException {
        String url = "https://api.github.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        String response = networkService.performHttpRequest(url, "GET", null, headers, null);
        assertNotNull(response);
        assertTrue(response.contains("current_user_url"));
    }
    
    @Test
    void testHttpRequestWithBody() throws IOException, InterruptedException {
        String url = "https://api.github.com";
        String body = "{\"name\":\"test\"}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String response = networkService.performHttpRequest(url, "POST", body, headers, "application/json");
        assertNotNull(response);
    }
    
    @Test
    void testDownloadFile() throws IOException, InterruptedException {
        String url = "https://raw.githubusercontent.com/octocat/Hello-World/master/README.md";
        byte[] content = networkService.downloadFile(url);
        assertNotNull(content);
        String contentStr = new String(content, java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(contentStr.contains("Hello World"));
    }
    
    @Test
    void testInvalidDownloadUrl() {
        String url = "https://this-is-not-a-real-file.com/file.txt";
        assertThrows(IOException.class, () -> {
            networkService.downloadFile(url);
        });
    }
    
    @Test
    void testHttpRequestWithAuth() throws IOException, InterruptedException {
        String url = "https://api.github.com";
        String username = "test";
        String password = "test";
        String response = networkService.performHttpRequestWithAuth(url, "GET", null, null, null, username, password);
        assertNotNull(response);
    }
    
    @Test
    void testHttpRequestWithToken() throws IOException, InterruptedException {
        String url = "https://api.github.com";
        String token = "test-token";
        String response = networkService.performHttpRequestWithToken(url, "GET", null, null, null, token);
        assertNotNull(response);
    }
} 