package by.shplau.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import by.shplau.util.FileValidator;
import by.shplau.util.ImageUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileValidator fileValidator;
    private final ImageProcessingService imageProcessingService;

    @Value("${file.output-format}")
    private String outputFormat;

    @Autowired
    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir,
            FileValidator fileValidator,
            ImageProcessingService imageProcessingService
    ) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.fileValidator = fileValidator;
        this.imageProcessingService = imageProcessingService;

        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation.resolve("thumbnails"));
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory for uploaded files.", ex);
        }
    }

    public ImageUploadResult storeFile(MultipartFile file) {
        fileValidator.validate(file);

        try {
            String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "." + outputFormat);

            // Обработка основного изображения
            byte[] processedImageBytes = imageProcessingService.processImageToBytes(file);
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.write(targetLocation, processedImageBytes);

            // Создание и сохранение миниатюры
            BufferedImage thumbnail = imageProcessingService.createThumbnail(ImageIO.read(file.getInputStream()));
            String thumbnailFileName = "thumb_" + fileName;
            Path thumbnailLocation = this.fileStorageLocation.resolve("thumbnails").resolve(thumbnailFileName);
            ImageIO.write(thumbnail, outputFormat, thumbnailLocation.toFile());

            return new ImageUploadResult(fileName, thumbnailFileName);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file.", ex);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename)
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            }
            throw new RuntimeException("File not found: " + filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName)
                    .normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't delete file: " + fileName);
        }
    }
}
