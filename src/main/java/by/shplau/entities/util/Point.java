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

    private double latitude, longitude;  // Исправлено название поля

    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;  // Добавлено описание точки

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    public Point (double latitude, double longitude, Type type, Route route) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.route = route;
    }
}
