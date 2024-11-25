package by.shplau.repositories;

import by.shplau.entities.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findAll();
    List<Route> findByDifficulty(int difficulty);
    List<Route> findByDurationLessThanEqual(int maxDuration);
}
