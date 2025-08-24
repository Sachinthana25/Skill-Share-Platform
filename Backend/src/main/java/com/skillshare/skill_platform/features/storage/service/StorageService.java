package com.linkedin.backend.features.storage.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
    private final Path rootLocation = Paths.get("uploads");

    public StorageService() {
        if (!rootLocation.toFile().exists()) {
            rootLocation.toFile().mkdir();
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!isImage(file.getContentType())) {
            throw new IllegalArgumentException("File is not an image");
        }

        if (isFileTooLarge(file)) {
            throw new IllegalArgumentException("File is too large");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + fileExtension;
        Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
        return fileName;
    }

    public FileInputStream getFileInputStream(String filename) throws IOException {
        Path file = rootLocation.resolve(filename);

        if (!file.getParent().equals(rootLocation)) { // Prevent directory traversal
            throw new IllegalArgumentException("Invalid file path");
        }

        if (!file.toFile().exists()) {
            throw new IllegalArgumentException("File not found");
        }

        return new FileInputStream(file.toFile());
    }

    public void deleteFile(String fileName) throws IOException {
        Path file = rootLocation.resolve(fileName);
        Files.delete(file);
    }

    public MediaType getMediaType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        switch (extension) {
            case ".png":
                return MediaType.IMAGE_PNG;
            case ".jpg":
            case ".jpeg":
                return MediaType.IMAGE_JPEG;
            case ".gif":
                return MediaType.IMAGE_GIF;
            case ".mp4":
                return MediaType.parseMediaType("video/mp4");
            case ".webm":
                return MediaType.parseMediaType("video/webm");
            case ".mov":
                return MediaType.parseMediaType("video/quicktime");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private boolean isImage(String contentType) {
        return contentType.startsWith("image");
    }

    private boolean isVideo(String contentType) {
        return contentType.startsWith("video");
    }

    private boolean isFileTooLarge(MultipartFile file) {
        if (isVideo(file.getContentType())) {
            return file.getSize() > 100 * 1024 * 1024; // 100MB for videos
        }
        return file.getSize() > 10 * 1024 * 1024; // 10MB for images
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (!isImage(file.getContentType()) && !isVideo(file.getContentType())) {
            throw new IllegalArgumentException("File must be an image or video");
        }

        if (isFileTooLarge(file)) {
            throw new IllegalArgumentException("File is too large");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + fileExtension;
        Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
        return fileName;
    }

}