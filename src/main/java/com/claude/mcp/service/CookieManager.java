package com.claude.mcp.service;

import com.claude.mcp.model.Cookie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CookieManager {
    private static final Logger logger = LoggerFactory.getLogger(CookieManager.class);
    private static final String COOKIE_STORE_FILE = "cookie_store.json";
    private final Map<String, Set<Cookie>> cookieStore;
    private final ScheduledExecutorService cleanupExecutor;
    private final ObjectMapper objectMapper;
    private final Path cookieStorePath;

    public CookieManager() {
        this.cookieStore = new ConcurrentHashMap<>();
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.cookieStorePath = Paths.get(COOKIE_STORE_FILE);
        
        loadCookies();
        startCleanupTask();
    }

    private void startCleanupTask() {
        cleanupExecutor.scheduleAtFixedRate(
            this::cleanExpiredCookies,
            1, 1, TimeUnit.HOURS
        );
    }

    public void addCookie(String domain, Cookie cookie) {
        Set<Cookie> cookies = cookieStore.computeIfAbsent(domain, k -> ConcurrentHashMap.newKeySet());
        cookies.removeIf(c -> c.getName().equals(cookie.getName()));
        cookies.add(cookie);
        saveCookies();
    }

    public Set<Cookie> getCookies(String domain) {
        return cookieStore.getOrDefault(domain, Collections.emptySet())
                         .stream()
                         .filter(cookie -> !cookie.isExpired())
                         .collect(Collectors.toSet());
    }

    public void removeCookie(String domain, String name) {
        Set<Cookie> cookies = cookieStore.get(domain);
        if (cookies != null) {
            cookies.removeIf(cookie -> cookie.getName().equals(name));
            saveCookies();
        }
    }

    public void clearCookies(String domain) {
        cookieStore.remove(domain);
        saveCookies();
    }

    public void clearAllCookies() {
        cookieStore.clear();
        saveCookies();
    }

    private void cleanExpiredCookies() {
        boolean hasChanges = false;
        for (Map.Entry<String, Set<Cookie>> entry : cookieStore.entrySet()) {
            int initialSize = entry.getValue().size();
            entry.getValue().removeIf(Cookie::isExpired);
            if (entry.getValue().size() < initialSize) {
                hasChanges = true;
            }
        }
        
        // Eliminar dominios sin cookies
        cookieStore.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        if (hasChanges) {
            saveCookies();
            logger.info("Limpieza de cookies expiradas completada");
        }
    }

    private void loadCookies() {
        try {
            if (Files.exists(cookieStorePath)) {
                String json = Files.readString(cookieStorePath);
                JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
                JavaType setType = objectMapper.getTypeFactory().constructCollectionType(Set.class, Cookie.class);
                JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, stringType, setType);
                Map<String, Set<Cookie>> loaded = objectMapper.readValue(json, mapType);
                cookieStore.putAll(loaded);
                cleanExpiredCookies();
                logger.info("Cookies cargadas desde almacenamiento persistente");
            }
        } catch (IOException e) {
            logger.error("Error al cargar cookies desde el archivo", e);
        }
    }

    private void saveCookies() {
        try {
            String json = objectMapper.writeValueAsString(cookieStore);
            Files.writeString(cookieStorePath, json, StandardOpenOption.CREATE, 
                            StandardOpenOption.TRUNCATE_EXISTING);
            logger.debug("Cookies guardadas en almacenamiento persistente");
        } catch (IOException e) {
            logger.error("Error al guardar cookies en el archivo", e);
        }
    }

    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }
} 