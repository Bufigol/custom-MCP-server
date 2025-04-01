package com.claude.mcp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkService {
    private static final Logger logger = LoggerFactory.getLogger(NetworkService.class);
    private final HttpClient httpClient;
    
    public NetworkService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    public NetworkService(int timeoutSeconds) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
    
    public String performHttpRequest(String url, String method, String body, 
            Map<String, String> headers, String contentType) throws IOException, InterruptedException {
        logger.info("Realizando petición {} a: {}", method, url);
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        
        // Añadir headers
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        
        // Añadir content type si se especifica
        if (contentType != null) {
            requestBuilder.header("Content-Type", contentType);
        }
        
        // Añadir body según el método
        if (body != null) {
            switch (method.toUpperCase()) {
                case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                case "PUT" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                case "PATCH" -> requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(body));
                case "DELETE" -> requestBuilder.DELETE();
                case "GET" -> requestBuilder.GET();
                default -> throw new IllegalArgumentException("Método HTTP no soportado: " + method);
            }
        } else {
            switch (method.toUpperCase()) {
                case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
                case "PUT" -> requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                case "PATCH" -> requestBuilder.method("PATCH", HttpRequest.BodyPublishers.noBody());
                case "DELETE" -> requestBuilder.DELETE();
                case "GET" -> requestBuilder.GET();
                default -> throw new IllegalArgumentException("Método HTTP no soportado: " + method);
            }
        }
        
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    public String performHttpGet(String url) throws IOException, InterruptedException {
        return performHttpRequest(url, "GET", null, null, null);
    }
    
    public String performHttpPost(String url, String body, String contentType) throws IOException, InterruptedException {
        return performHttpRequest(url, "POST", body, null, contentType);
    }
    
    public String performHttpPut(String url, String body, String contentType) throws IOException, InterruptedException {
        return performHttpRequest(url, "PUT", body, null, contentType);
    }
    
    public String performHttpDelete(String url) throws IOException, InterruptedException {
        return performHttpRequest(url, "DELETE", null, null, null);
    }
    
    public String performHttpPatch(String url, String body, String contentType) throws IOException, InterruptedException {
        return performHttpRequest(url, "PATCH", body, null, contentType);
    }
    
    public String performHttpRequestWithAuth(String url, String method, String body, 
            Map<String, String> headers, String contentType, String username, String password) 
            throws IOException, InterruptedException {
        Map<String, String> authHeaders = Map.of(
            "Authorization", "Basic " + java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes())
        );
        
        Map<String, String> finalHeaders = headers != null ? 
            Map.copyOf(headers) : Map.of();
        
        return performHttpRequest(url, method, body, finalHeaders, contentType);
    }
    
    public String performHttpRequestWithToken(String url, String method, String body, 
            Map<String, String> headers, String contentType, String token) 
            throws IOException, InterruptedException {
        Map<String, String> authHeaders = Map.of(
            "Authorization", "Bearer " + token
        );
        
        Map<String, String> finalHeaders = headers != null ? 
            Map.copyOf(headers) : Map.of();
        
        return performHttpRequest(url, method, body, finalHeaders, contentType);
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
    
    public byte[] downloadFile(String url) throws IOException, InterruptedException {
        logger.info("Descargando archivo de: {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }
} 