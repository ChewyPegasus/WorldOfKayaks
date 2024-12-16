package by.shplau.entities;

import by.shplau.entities.util.Point;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String riverName;
    private String description;  // Добавлено описание маршрута

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Point> points;

    //in minutes
    private int duration;

    private int difficulty = 0;
    private double price;
    private String imageUrl;
    private String thumbnailUrl;

    public Route(Route other) {
        this.difficulty = other.getDifficulty();
        this.price = other.getPrice();
        this.duration = other.getDuration();
        this.imageUrl = other.getImageUrl();
        this.points = other.getPoints();
        this.riverName = other.getRiverName();
        this.description = other.getDescription();
        this.thumbnailUrl = other.getThumbnailUrl();
    }
}
