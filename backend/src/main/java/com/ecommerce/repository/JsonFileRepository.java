package com.ecommerce.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class JsonFileRepository {

    private final ObjectMapper objectMapper;
    private final String dataDir;

    public JsonFileRepository(@Value("${app.data.dir:data}") String dataDir) {
        this.dataDir = dataDir;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ensureDataDir();
    }

    private void ensureDataDir() {
        try {
            Files.createDirectories(Paths.get(dataDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create data directory", e);
        }
    }

    private File getFile(String filename) {
        return new File(dataDir, filename);
    }

    public <T> List<T> readAll(String filename, TypeReference<List<T>> typeRef) {
        File file = getFile(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public <T> void writeAll(String filename, List<T> data) {
        try {
            objectMapper.writeValue(getFile(filename), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON file: " + filename, e);
        }
    }

    public <T> Optional<T> findById(String filename, String id, TypeReference<List<T>> typeRef, Predicate<T> idMatcher) {
        return readAll(filename, typeRef).stream().filter(idMatcher).findFirst();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
