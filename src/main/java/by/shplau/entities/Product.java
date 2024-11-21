package by.shplau.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
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
public class Product extends AbstractEntity {
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
    private int price;
    private Category category;
    private String imageURL;
}
