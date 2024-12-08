package by.shplau.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import by.shplau.util.FileValidator;
import by.shplau.util.ImageUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    private final Path productStorageLocation;
    private final Path routeStorageLocation;
    private final FileValidator fileValidator;
    private final ImageProcessingService imageProcessingService;

    @Value("${file.output-format}")
    private String outputFormat;

    @Autowired
    public FileStorageService(
            FileValidator fileValidator,
            ImageProcessingService imageProcessingService
    ) {
        // Using the existing static resources structure
        this.productStorageLocation = Paths.get("src/main/resources/static/img/samples/products").toAbsolutePath().normalize();
        this.routeStorageLocation = Paths.get("src/main/resources/static/img/samples/routes").toAbsolutePath().normalize();
        this.fileValidator = fileValidator;
        this.imageProcessingService = imageProcessingService;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            // Try to find in products directory
            Path filePath = productStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }

            // Try routes directory
            filePath = routeStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }

            // Try default directory
            Resource defaultResource = new ClassPathResource("static/img/default/" + fileName);
            if (defaultResource.exists()) {
                return defaultResource;
            }

            throw new RuntimeException("File not found: " + fileName);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }

    public ImageUploadResult storeProductImage(MultipartFile file) {
        return storeFile(file, productStorageLocation);
    }

    public ImageUploadResult storeRouteImage(MultipartFile file) {
        return storeFile(file, routeStorageLocation);
    }

    private ImageUploadResult storeFile(MultipartFile file, Path storageLocation) {
        fileValidator.validate(file);

        try {
            String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "." + outputFormat);

            // Ensure the directory exists
            Files.createDirectories(storageLocation);

            // Save the original image
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage processedImage = imageProcessingService.processImage(originalImage);
            ImageIO.write(processedImage, outputFormat,
                    storageLocation.resolve(fileName).toFile());

            return new ImageUploadResult(fileName, fileName); // Using same name as we don't create thumbnails
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file", ex);
        }
    }

    public void deleteFile(String fileName) {
        try {
            // Try to delete from products directory
            Path productPath = productStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(productPath);

            // Try to delete from routes directory
            Path routePath = routeStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(routePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file: " + fileName, ex);
        }
    }
}