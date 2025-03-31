package com.claude.mcp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    public String readFile(String filePath) throws IOException {
        logger.info("Leyendo archivo: {}", filePath);
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
    
    public void writeFile(String filePath, String content) throws IOException {
        logger.info("Escribiendo archivo: {}", filePath);
        Path path = Paths.get(filePath);
        Files.writeString(path, content);
    }
    
    public List<String> listDirectory(String directoryPath) throws IOException {
        logger.info("Listando directorio: {}", directoryPath);
        Path path = Paths.get(directoryPath);
        return Files.list(path)
                .map(Path::toString)
                .toList();
    }
    
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
} 