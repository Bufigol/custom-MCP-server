package com.claude.mcp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NetworkService {
    private static final Logger logger = LoggerFactory.getLogger(NetworkService.class);
    private final HttpClient httpClient;
    
    public NetworkService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    public String performHttpGet(String url) throws IOException, InterruptedException {
        logger.info("Realizando petición GET a: {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    public String performHttpPost(String url, String body, String contentType) throws IOException, InterruptedException {
        logger.info("Realizando petición POST a: {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    public boolean isUrlAccessible(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .HEAD()
                    .build();
            
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            logger.error("Error al verificar URL: {}", url, e);
            return false;
        }
    }
} 