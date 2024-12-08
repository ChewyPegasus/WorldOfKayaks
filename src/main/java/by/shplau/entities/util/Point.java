package by.shplau.entities.util;

import by.shplau.entities.Route;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {
    public enum Type {
        START,
        END,
        REST,
        FOOD,
        SIGHT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude, longtitude;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    public Point (double latitude, double longtitude, Type type, Route route) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.type = type;
        this.route = route;
    }
}
