package by.shplau.entities.util;

import by.shplau.entities.Route;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude, longtitude;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
