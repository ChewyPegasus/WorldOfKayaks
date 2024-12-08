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
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String riverName;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Point> points;

    //in minutes
    private int duration;

    private int difficulty = 0;
    private double price;
    private String imageUrl;

    public Route(Route other) {
        this.difficulty = other.getDifficulty();
        this.price = other.getPrice();
        this.duration = other.getDuration();
        this.imageUrl = other.getImageUrl();
        this.points = other.getPoints();
        this.riverName = other.getRiverName();
    }
}
