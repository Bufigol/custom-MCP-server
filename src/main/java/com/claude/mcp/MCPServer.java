package com.claude.mcp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.claude.mcp.model.Message;
import com.claude.mcp.service.DatabaseService;
import com.claude.mcp.service.FileService;
import com.claude.mcp.service.NetworkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MCPServer {
    private static final Logger logger = LoggerFactory.getLogger(MCPServer.class);
    private final FileService fileService;
    private final DatabaseService databaseService;
    private final NetworkService networkService;
    private final ObjectMapper objectMapper;
    
    public MCPServer() {
        this.fileService = new FileService();
        this.databaseService = new DatabaseService();
        this.networkService = new NetworkService();
        this.objectMapper = new ObjectMapper();
    }
    
    public String processMessage(String messageJson) {
        try {
            Message message = objectMapper.readValue(messageJson, Message.class);
            Message response = new Message();
            
            switch (message.getType()) {
                case FILE_READ -> handleFileRead(message, response);
                case FILE_WRITE -> handleFileWrite(message, response);
                case NETWORK_REQUEST -> handleNetworkRequest(message, response);
                case DATABASE_QUERY -> handleDatabaseQuery(message, response);
                default -> {
                    response.setType(Message.MessageType.ERROR);
                    response.setContent("Tipo de mensaje no soportado: " + message.getType());
                }
            }
            
            return objectMapper.writeValueAsString(response);
        } catch (IOException e) {
            logger.error("Error de entrada/salida al procesar mensaje", e);
            try {
                Message errorResponse = new Message();
                errorResponse.setType(Message.MessageType.ERROR);
                errorResponse.setContent("Error de entrada/salida: " + e.getMessage());
                return objectMapper.writeValueAsString(errorResponse);
            } catch (IOException ex) {
                return "{\"type\":\"ERROR\",\"content\":\"Error interno del servidor al procesar respuesta\"}";
            }
        } catch (SQLException e) {
            logger.error("Error de base de datos al procesar mensaje", e);
            try {
                Message errorResponse = new Message();
                errorResponse.setType(Message.MessageType.ERROR);
                errorResponse.setContent("Error de base de datos: " + e.getMessage());
                return objectMapper.writeValueAsString(errorResponse);
            } catch (IOException ex) {
                return "{\"type\":\"ERROR\",\"content\":\"Error interno del servidor al procesar respuesta\"}";
            }
        } catch (InterruptedException e) {
            logger.error("Operación interrumpida al procesar mensaje", e);
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            try {
                Message errorResponse = new Message();
                errorResponse.setType(Message.MessageType.ERROR);
                errorResponse.setContent("Operación interrumpida: " + e.getMessage());
                return objectMapper.writeValueAsString(errorResponse);
            } catch (IOException ex) {
                return "{\"type\":\"ERROR\",\"content\":\"Error interno del servidor al procesar respuesta\"}";
            }
        }
    }
    
    private void handleFileRead(Message message, Message response) throws IOException {
        Map<String, String> params = (Map<String, String>) message.getParameters();
        String filePath = params.get("filePath");
        
        if (filePath == null) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Se requiere filePath en los parámetros");
            return;
        }
        
        String content = fileService.readFile(filePath);
        response.setType(Message.MessageType.RESPONSE);
        response.setContent(content);
    }
    
    private void handleFileWrite(Message message, Message response) throws IOException {
        Map<String, String> params = (Map<String, String>) message.getParameters();
        String filePath = params.get("filePath");
        String content = params.get("content");
        
        if (filePath == null || content == null) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Se requieren filePath y content en los parámetros");
            return;
        }
        
        fileService.writeFile(filePath, content);
        response.setType(Message.MessageType.RESPONSE);
        response.setContent("Archivo escrito exitosamente");
    }
    
    public void handleNetworkRequest(Message message, Message response) throws IOException, InterruptedException, JsonProcessingException {
        Map<String, Object> params = (Map<String, Object>) message.getParameters();
        String url = (String) params.get("url");
        String method = (String) params.get("method");
        String body = (String) params.get("body");
        String contentType = (String) params.get("contentType");
        Map<String, String> headers = (Map<String, String>) params.get("headers");
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String token = (String) params.get("token");
        Boolean downloadFile = (Boolean) params.get("downloadFile");
        
        if (url == null) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Se requiere url en los parámetros");
            return;
        }
        
        try {
            String result;
            if (downloadFile != null && downloadFile) {
                byte[] fileContent = networkService.downloadFile(url);
                response.setType(Message.MessageType.RESPONSE);
                response.setContent(java.util.Base64.getEncoder().encodeToString(fileContent));
                return;
            }
            
            if (username != null && password != null) {
                result = networkService.performHttpRequestWithAuth(url, method, body, headers, contentType, username, password);
            } else if (token != null) {
                result = networkService.performHttpRequestWithToken(url, method, body, headers, contentType, token);
            } else {
                result = networkService.performHttpRequest(url, method, body, headers, contentType);
            }
            
            response.setType(Message.MessageType.RESPONSE);
            response.setContent(result);
        } catch (IllegalArgumentException e) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("URL no válida o no permitida: " + e.getMessage());
        }
    }
    
    private void handleDatabaseQuery(Message message, Message response) throws SQLException, JsonProcessingException {
        Map<String, Object> params = (Map<String, Object>) message.getParameters();
        String connectionId = (String) params.get("connectionId");
        String query = (String) params.get("query");
        Object[] queryParams = (Object[]) params.get("queryParams");
        
        if (connectionId == null || query == null) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Se requieren connectionId y query en los parámetros");
            return;
        }
        
        if (query.toLowerCase().startsWith("select")) {
            var results = databaseService.executeQuery(connectionId, query, queryParams);
            response.setType(Message.MessageType.RESPONSE);
            response.setContent(objectMapper.writeValueAsString(results));
        } else {
            int affectedRows = databaseService.executeUpdate(connectionId, query, queryParams);
            response.setType(Message.MessageType.RESPONSE);
            response.setContent("Filas afectadas: " + affectedRows);
        }
    }
} 