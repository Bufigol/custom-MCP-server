package com.claude.mcp.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    
    @Test
    void testValidUrls() {
        assertTrue(UrlValidator.isValidUrl("https://api.example.com"));
        assertTrue(UrlValidator.isValidUrl("http://api.example.com"));
        assertTrue(UrlValidator.isValidUrl("https://api.example.com:8080"));
        assertTrue(UrlValidator.isValidUrl("https://api.example.com/path"));
        assertTrue(UrlValidator.isValidUrl("https://api.example.com/path?param=value"));
    }
    
    @Test
    void testInvalidProtocols() {
        assertFalse(UrlValidator.isValidUrl("ftp://api.example.com"));
        assertFalse(UrlValidator.isValidUrl("sftp://api.example.com"));
        assertFalse(UrlValidator.isValidUrl("file:///path/to/file"));
    }
    
    @Test
    void testBlockedDomains() {
        assertFalse(UrlValidator.isValidUrl("http://localhost"));
        assertFalse(UrlValidator.isValidUrl("https://127.0.0.1"));
        assertFalse(UrlValidator.isValidUrl("http://0.0.0.0"));
        assertFalse(UrlValidator.isValidUrl("https://localhost:8080"));
    }
    
    @Test
    void testInvalidUrls() {
        assertFalse(UrlValidator.isValidUrl("not a url"));
        assertFalse(UrlValidator.isValidUrl(""));
        assertFalse(UrlValidator.isValidUrl(null));
        assertFalse(UrlValidator.isValidUrl("http://"));
        assertFalse(UrlValidator.isValidUrl("https://"));
    }
    
    @Test
    void testInvalidPorts() {
        assertFalse(UrlValidator.isValidUrl("http://api.example.com:0"));
        assertFalse(UrlValidator.isValidUrl("https://api.example.com:65536"));
        assertFalse(UrlValidator.isValidUrl("http://api.example.com:-1"));
    }
} 