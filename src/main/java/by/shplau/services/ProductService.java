package by.shplau.services;

import by.shplau.entities.Product;
import by.shplau.repositories.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final Path PRODUCTS_PATH = Paths.get("src/main/resources/static/img/samples/products").toAbsolutePath().normalize();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product, MultipartFile file) {
        // Ensure a default image is set if no image is provided
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (!Files.exists(PRODUCTS_PATH)) {
                    Files.createDirectories(PRODUCTS_PATH);
                }
                Files.copy(file.getInputStream(), PRODUCTS_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setImageURL(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + fileName, e);
            }
        } else if (product.getImageURL() == null || product.getImageURL().isEmpty()) {
            // Set a default product image if no image is provided
            product.setImageURL("default_product.jpg");
        }
        return productRepository.save(product);
    }

    public Product updateProductImage(Long id, Product product, MultipartFile image) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such product to be updated"));

        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (existingProduct.getImageURL() != null) {
                String oldFileName = existingProduct.getImageURL().substring(
                        existingProduct.getImageURL().lastIndexOf("/") + 1);
                try {
                    Files.deleteIfExists(PRODUCTS_PATH.resolve(oldFileName));
                } catch (IOException e) {
                    // Log error but continue
                    e.printStackTrace();
                }
            }

            // Save new image
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            try {
                Files.copy(image.getInputStream(), PRODUCTS_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setImageURL("img/samples/products/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + fileName, e);
            }
        }

        // Update other fields
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        if (product.getImageURL() != null) {
            existingProduct.setImageURL(product.getImageURL());
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No product to be deleted"));

        // Delete associated image
        if (product.getImageURL() != null) {
            String fileName = product.getImageURL().substring(
                    product.getImageURL().lastIndexOf("/") + 1);
            try {
                Files.deleteIfExists(PRODUCTS_PATH.resolve(fileName));
            } catch (IOException e) {
                // Log error but continue with product deletion
                e.printStackTrace();
            }
        }

        productRepository.delete(product);
    }
}