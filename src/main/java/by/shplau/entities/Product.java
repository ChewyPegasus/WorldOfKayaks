package by.shplau.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Это класс продукта или услуги
 * Соответствует <b>Product</b> в базе данных
 * @author chewy_pegasus
 * @since 19.11.2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    public Product(String name, String description, double price, Category category, String imageURL) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageURL = imageURL;
    }

    public Product(Product other) {
        this.name = other.getName();
        this.description = other.getDescription();
        this.price = other.getPrice();
        this.category = other.getCategory();
        this.imageURL = other.getImageURL();
        this.thumbnailURL = other.getThumbnailURL();
    }

    public enum Category {
        PRODUCT,
        SERVICE,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imageURL;
    private String thumbnailURL;
}
