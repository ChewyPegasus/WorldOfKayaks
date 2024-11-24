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
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hours;
    private int minutes;

    @OneToOne(mappedBy = "duration")
    private Route route;
}
