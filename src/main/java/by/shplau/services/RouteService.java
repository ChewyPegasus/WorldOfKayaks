package by.shplau.services;

import by.shplau.entities.Route;
import by.shplau.repositories.RouteRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> findRoutesByDifficulty(int difficulty) {
        return routeRepository.findByDifficulty(difficulty);
    }

    public List<Route> getRoutesByDuration(int maxDuration) {
        return routeRepository.findByDurationLessThanEqual(maxDuration);
    }
}
