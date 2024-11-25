package by.shplau.controllers;

import by.shplau.entities.Route;
import by.shplau.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Route>> getRoutes(
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) Integer maxDuration) {
        List<Route> routes;

        if (difficulty != null) {
            routes = routeService.findRoutesByDifficulty(difficulty);
        } else {
            routes = routeService.getAllRoutes();
        }

        if (maxDuration != null) {
            routes = routes.stream()
                    .filter(e -> e.getDuration() <= maxDuration)
                    .toList();
        }

        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Optional<Route> route = routeService.findRouteById(id);
        return route.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public  ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route createdRoute = routeService.createRoute(route);
        return ResponseEntity.ok(createdRoute);
    }
}
