package com.claude.mcp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private Map<String, Connection> connections = new HashMap<>();
    
    public void connect(String connectionId, String url, String username, String password) throws SQLException {
        logger.info("Conectando a base de datos: {}", url);
        Connection connection = DriverManager.getConnection(url, username, password);
        connections.put(connectionId, connection);
    }
    
    public void disconnect(String connectionId) throws SQLException {
        Connection connection = connections.remove(connectionId);
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    public List<Map<String, Object>> executeQuery(String connectionId, String query, Object... params) throws SQLException {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new SQLException("No hay conexión activa con ID: " + connectionId);
        }
        
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }
    
    public int executeUpdate(String connectionId, String query, Object... params) throws SQLException {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new SQLException("No hay conexión activa con ID: " + connectionId);
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }
} 