package by.shplau.services;

import java.awt.image.BufferedImage;
import java.io.File;
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
import java.nio.file.StandardCopyOption;
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
            @Value("${file.upload-dir.products}") String productUploadDir,
            @Value("${file.upload-dir.routes}") String routeUploadDir,
            FileValidator fileValidator,
            ImageProcessingService imageProcessingService
    ) {
        this.productStorageLocation = Paths.get(productUploadDir).toAbsolutePath().normalize();
        this.routeStorageLocation = Paths.get(routeUploadDir).toAbsolutePath().normalize();
        this.fileValidator = fileValidator;
        this.imageProcessingService = imageProcessingService;

        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(productStorageLocation);
            Files.createDirectories(productStorageLocation.resolve("thumbnails"));
            Files.createDirectories(routeStorageLocation);
            Files.createDirectories(routeStorageLocation.resolve("thumbnails"));
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directories", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            // Сначала пробуем найти в директории продуктов
            Path filePath = productStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }

            // Если не нашли, ищем в директории маршрутов
            filePath = routeStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }

            // Проверяем в директориях миниатюр
            filePath = productStorageLocation.resolve("thumbnails").resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }

            filePath = routeStorageLocation.resolve("thumbnails").resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
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
            String thumbnailFileName = "thumb_" + fileName;

            // Сохраняем основное изображение
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage processedImage = imageProcessingService.processImage(originalImage);
            ImageIO.write(processedImage, outputFormat,
                    storageLocation.resolve(fileName).toFile());

            // Создаем и сохраняем миниатюру
            BufferedImage thumbnail = imageProcessingService.createThumbnail(originalImage);
            ImageIO.write(thumbnail, outputFormat,
                    storageLocation.resolve("thumbnails").resolve(thumbnailFileName).toFile());

            return new ImageUploadResult(fileName, thumbnailFileName);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file", ex);
        }
    }

    // Метод для копирования sample изображений при инициализации
    public void copySampleImages() {
        try {
            Resource productSamples = new ClassPathResource("static/img/samples/products");
            Resource routeSamples = new ClassPathResource("static/img/samples/routes");

            copyResourceFiles(productSamples, productStorageLocation);
            copyResourceFiles(routeSamples, routeStorageLocation);
        } catch (IOException ex) {
            log.error("Error copying sample images", ex);
        }
    }

    private void copyResourceFiles(Resource source, Path destination) throws IOException {
        File sourceFolder = source.getFile();
        if (sourceFolder.exists() && sourceFolder.isDirectory()) {
            for (File file : sourceFolder.listFiles()) {
                Files.copy(file.toPath(),
                        destination.resolve(file.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void deleteFile(String fileName) {
        try {
            // Удаляем основной файл
            Path filePath = productStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);

            // Удаляем миниатюру
            String thumbnailName = "thumb_" + fileName;
            Path thumbnailPath = productStorageLocation.resolve("thumbnails").resolve(thumbnailName).normalize();
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file: " + fileName, ex);
        }
    }
}
