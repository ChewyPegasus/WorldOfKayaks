package by.shplau.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
public class FileValidator {
    private final long maxFileSize;
    private final List<String> allowedTypes;
    private final Pattern fileNamePattern = Pattern.compile("[a-zA-Z0-9-_]");

    public FileValidator(long maxFileSize, List<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
        this.maxFileSize = maxFileSize;
    }

    public void validate(MultipartFile file) {
        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("File exceeds maxSize: " + file.getName());
        }

        if (!allowedTypes.contains(file.getContentType())) {
            throw new RuntimeException("File type is not allowed: " + file.getContentType());
        }

        if (!allowedTypes.contains(file.getContentType())) {
            throw new RuntimeException("File type is not allowed: " + file.getContentType());
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!fileNamePattern.matcher(fileName).matches()) {
            throw new RuntimeException("Invalid file name: " + fileName);
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new RuntimeException("Invalid image file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error validating image file", e);
        }
    }
}
