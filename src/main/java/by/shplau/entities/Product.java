package by.shplau.entities;

/**
 * Это класс продукта или услуги
 * Соответствует <b>Product</b> в базе данных
 * @author chewy_pegasus
 * @since 19.11.2024
 */
public class Product extends AbstractEntity {
    public enum Category {
        PRODUCT,
        SERVICE,
        OTHER
    }

    private String name;
    private String description;
    private int price;
    private Category category;
    private String imageURL;
}
