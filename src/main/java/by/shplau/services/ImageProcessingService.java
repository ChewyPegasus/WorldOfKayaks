package by.shplau.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
@Slf4j
public class ImageProcessingService {
    @Value("${file.thumbnail-size}")
    private int thumbnailSize;

    @Value("${file.max-image-dimension}")
    private int maxImageDimension;

    @Value("${file.output-format}")
    private String outputFormat;

    @Value("${file.jpeg-quality}")
    private float jpegQuality;

    public BufferedImage processImage(MultipartFile file) throws IOException {
        BufferedImage original = ImageIO.read(file.getInputStream());

        // Оптимизация размера
        BufferedImage resized = resizeImage(original);

        // Создание миниатюры
        BufferedImage thumbnail = createThumbnail(original);

        return resized;
    }

    public byte[] processImageToBytes(MultipartFile file) throws IOException {
        BufferedImage processed = processImage(file);
        return imageToBytes(processed);
    }

    private BufferedImage resizeImage(BufferedImage original) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        // Проверяем, нужно ли уменьшать изображение
        if (originalWidth <= maxImageDimension && originalHeight <= maxImageDimension) {
            return original;
        }

        // Вычисляем новые размеры, сохраняя пропорции
        double scale = Math.min(
                (double) maxImageDimension / originalWidth,
                (double) maxImageDimension / originalHeight
        );

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();

        // Включаем сглаживание для лучшего качества
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resized;
    }

    public BufferedImage createThumbnail(BufferedImage original) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        double scale = (double) thumbnailSize / Math.max(originalWidth, originalHeight);
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return thumbnail;
    }

    private byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Настраиваем качество JPEG
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(outputFormat);
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(jpegQuality);
        }

        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);

        writer.dispose();
        ios.close();

        return baos.toByteArray();
    }
}
