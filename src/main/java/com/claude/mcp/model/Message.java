package com.claude.mcp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("type")
    private MessageType type;
    
    @JsonProperty("content")
    private String content;
    
    @JsonProperty("parameters")
    private Object parameters;
    
    public enum MessageType {
        FILE_READ,
        FILE_WRITE,
        NETWORK_REQUEST,
        DATABASE_QUERY,
        RESPONSE,
        ERROR
    }
    
    // Getters y Setters
    public MessageType getType() {
        return type;
    }
    
    public void setType(MessageType type) {
        this.type = type;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Object getParameters() {
        return parameters;
    }
    
    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
} 