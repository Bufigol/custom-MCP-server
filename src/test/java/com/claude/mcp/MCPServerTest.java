package com.claude.mcp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import com.claude.mcp.model.Message;
import com.claude.mcp.model.Message.MessageType;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;

class MCPServerTest {
    
    private MCPServer server;
    
    @BeforeEach
    void setUp() {
        server = new MCPServer();
    }
    
    @Test
    void testValidNetworkRequest() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("url", "https://api.github.com");
        params.put("method", "GET");
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.RESPONSE, response.getType());
        assertNotNull(response.getContent());
    }
    
    @Test
    void testInvalidUrlNetworkRequest() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("url", "not a valid url");
        params.put("method", "GET");
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.ERROR, response.getType());
        assertTrue(response.getContent().contains("URL inválida"));
    }
    
    @Test
    void testBlockedDomainNetworkRequest() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("url", "http://localhost");
        params.put("method", "GET");
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.ERROR, response.getType());
        assertTrue(response.getContent().contains("URL no permitida"));
    }
    
    @Test
    void testNetworkRequestWithHeaders() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("url", "https://api.github.com");
        params.put("method", "GET");
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        params.put("headers", headers);
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.RESPONSE, response.getType());
        assertNotNull(response.getContent());
    }
    
    @Test
    void testNetworkRequestWithBody() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("url", "https://api.github.com");
        params.put("method", "POST");
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        params.put("headers", headers);
        
        String body = "{\"name\":\"test\"}";
        params.put("body", body);
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.RESPONSE, response.getType());
        assertNotNull(response.getContent());
    }
    
    @Test
    void testInvalidRequestType() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.FILE_READ);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.ERROR, response.getType());
        assertTrue(response.getContent().contains("Tipo de solicitud no soportado"));
    }
    
    @Test
    void testMissingUrl() throws IOException, InterruptedException, JsonProcessingException {
        Message request = new Message();
        request.setType(MessageType.NETWORK_REQUEST);
        Map<String, Object> params = new HashMap<>();
        params.put("method", "GET");
        request.setParameters(params);
        
        Message response = new Message();
        server.handleNetworkRequest(request, response);
        
        assertEquals(MessageType.ERROR, response.getType());
        assertTrue(response.getContent().contains("URL requerida"));
    }
} 