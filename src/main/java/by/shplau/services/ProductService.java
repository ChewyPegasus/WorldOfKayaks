package by.shplau.services;

import by.shplau.entities.Product;
import by.shplau.repositories.ProductRepository;
import by.shplau.util.ImageUploadResult;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

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

    public Product createProduct(Product product, MultipartFile image) {
        ImageUploadResult imageResult = fileService.storeProductImage(image);
        product.setImageURL("files/download/" + imageResult.getImageFileName());
        product.setThumbnailURL("files/download/thumbnails/" + imageResult.getThumbnailFileName());
        return productRepository.save(product);
    }

    public Product updateProductImage(Long id, Product product, MultipartFile image) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No such product to be updated"));
        if (image != null) {
            String oldFileName = existingProduct
                    .getImageURL()
                    .substring(existingProduct
                            .getImageURL()
                            .lastIndexOf("/") + 1);
            fileService.deleteFile(oldFileName);

            ImageUploadResult imageResult = fileService.storeProductImage(image);
            product.setImageURL("files/download/" + imageResult.getImageFileName());
            product.setThumbnailURL("files/download/thumbnails/" + imageResult.getThumbnailFileName());
            return productRepository.save(product);
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        if (product.getImageURL() != null) {
            existingProduct.setImageURL(product.getImageURL());
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No product to be deleted"));
        String fileName = product.getImageURL()
                .substring(product
                        .getImageURL()
                        .lastIndexOf("/") + 1);
        fileService.deleteFile(fileName);
        productRepository.delete(product);
    }
}
