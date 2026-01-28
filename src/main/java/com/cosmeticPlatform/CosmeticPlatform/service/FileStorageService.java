package com.cosmeticPlatform.CosmeticPlatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".webp");
    private static final int MAX_WIDTH = 800;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        }

        // Validate extension
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new RuntimeException("Invalid file type. Only JPG, PNG, and WebP are allowed.");
        }

        // Generate unique file name
        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            // Resize and Save Image
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                // Determine if it was actually an image or something else handled by extensions
                // check
                // If it's not a readable image, just save it as is if extension was allowed, or
                // throw
                // Here we assume if ImageIO can't read it, we save it as valid allowed file
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            }

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // Only resize if width > MAX_WIDTH
            if (originalWidth > MAX_WIDTH) {
                int newWidth = MAX_WIDTH;
                int newHeight = (int) ((double) originalHeight / originalWidth * newWidth);

                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                g.dispose();

                // Save resized
                String formatName = fileExtension.replace(".", "");
                if (formatName.equals("jpg"))
                    formatName = "jpeg";

                ImageIO.write(resizedImage, formatName, targetLocation.toFile());
            } else {
                // Save original if small enough
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
