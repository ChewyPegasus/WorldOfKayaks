package by.shplau.controllers;

import by.shplau.entities.Product;
import by.shplau.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductContoller {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        products.forEach(product -> {
            if (product.getImageURL() != null) {
                product.setImageURL("/img/samples/products/" + product.getImageURL());
            }
            if (product.getThumbnailURL() != null) {
                product.setThumbnailURL("/img/samples/products/" + product.getThumbnailURL());
            }
        });
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getImageURL() != null) {
                product.setImageURL("/img/samples/products/" + product.getImageURL());
            }
            if (product.getThumbnailURL() != null) {
                product.setThumbnailURL("/img/samples/products/" + product.getThumbnailURL());
            }
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }   

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam("file") MultipartFile file, @RequestParam("product") String productJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Product product = mapper.readValue(productJson, Product.class);
            return ResponseEntity.ok(productService.createProduct(product, file));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid product data", e);
        }
    }
}
