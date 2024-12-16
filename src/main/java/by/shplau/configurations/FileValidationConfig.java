package by.shplau.configurations;

import by.shplau.util.FileValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FileValidationConfig {

    @Value("${file.max-size}")
    private long maxFileSize; // in Bytes

    @Value("${file.allowed-types}")
    private List<String> allowedTypes;

    @Value("${file.thumbnail-size}")
    private int thumbnailSize; // in pixels

    @Value("${file.max-image-dimension}")
    private int maxImageDimension; // in pixels

    @Bean
    public FileValidator fileValidator() {
        return new FileValidator(maxFileSize, allowedTypes);
    }
}
