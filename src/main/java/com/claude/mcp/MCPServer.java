package com.claude.mcp;

import com.claude.mcp.model.Message;
import com.claude.mcp.service.DatabaseService;
import com.claude.mcp.service.FileService;
import com.claude.mcp.service.NetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

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
        } catch (Exception e) {
            logger.error("Error procesando mensaje", e);
            try {
                Message errorResponse = new Message();
                errorResponse.setType(Message.MessageType.ERROR);
                errorResponse.setContent("Error procesando mensaje: " + e.getMessage());
                return objectMapper.writeValueAsString(errorResponse);
            } catch (IOException ex) {
                return "{\"type\":\"ERROR\",\"content\":\"Error interno del servidor\"}";
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
    
    private void handleNetworkRequest(Message message, Message response) throws IOException, InterruptedException {
        Map<String, String> params = (Map<String, String>) message.getParameters();
        String url = params.get("url");
        String method = params.get("method");
        String body = params.get("body");
        
        if (url == null) {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Se requiere url en los parámetros");
            return;
        }
        
        String result;
        if ("GET".equalsIgnoreCase(method)) {
            result = networkService.performHttpGet(url);
        } else if ("POST".equalsIgnoreCase(method)) {
            result = networkService.performHttpPost(url, body, "application/json");
        } else {
            response.setType(Message.MessageType.ERROR);
            response.setContent("Método HTTP no soportado: " + method);
            return;
        }
        
        response.setType(Message.MessageType.RESPONSE);
        response.setContent(result);
    }
    
    private void handleDatabaseQuery(Message message, Message response) throws SQLException {
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