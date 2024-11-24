package by.shplau.entities;

import by.shplau.entities.util.Point;
import by.shplau.entities.util.Time;
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
    private List<Point> points;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_id")
    private Time duration;

    private int difficulty;
    private double price;
    private String imageUrl;
}
