package com.claude.mcp.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlValidator {
    private static final Logger logger = LoggerFactory.getLogger(UrlValidator.class);
    private static final Set<String> ALLOWED_PROTOCOLS = Set.of("http", "https");
    private static final Set<String> BLOCKED_DOMAINS = new HashSet<>();
    
    static {
        // Inicializar dominios bloqueados
        BLOCKED_DOMAINS.add("localhost");
        BLOCKED_DOMAINS.add("127.0.0.1");
        BLOCKED_DOMAINS.add("0.0.0.0");
    }
    
    public static boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            
            // Validar protocolo
            if (!ALLOWED_PROTOCOLS.contains(uri.getScheme().toLowerCase())) {
                logger.warn("Protocolo no permitido: {}", uri.getScheme());
                return false;
            }
            
            // Validar dominio
            String host = uri.getHost();
            if (host == null || host.isEmpty()) {
                logger.warn("Host no válido en URL: {}", url);
                return false;
            }
            
            // Verificar dominios bloqueados
            for (String blockedDomain : BLOCKED_DOMAINS) {
                if (host.equalsIgnoreCase(blockedDomain) || host.endsWith("." + blockedDomain)) {
                    logger.warn("Dominio bloqueado detectado: {}", host);
                    return false;
                }
            }
            
            // Validar puerto (opcional)
            int port = uri.getPort();
            if (port != -1 && (port < 1 || port > 65535)) {
                logger.warn("Puerto no válido: {}", port);
                return false;
            }
            
            return true;
        } catch (URISyntaxException e) {
            logger.warn("URL no válida: {}", url, e);
            return false;
        }
    }
    
    public static void addBlockedDomain(String domain) {
        if (domain != null && !domain.isEmpty()) {
            BLOCKED_DOMAINS.add(domain.toLowerCase());
        }
    }
    
    public static void removeBlockedDomain(String domain) {
        if (domain != null) {
            BLOCKED_DOMAINS.remove(domain.toLowerCase());
        }
    }
} 