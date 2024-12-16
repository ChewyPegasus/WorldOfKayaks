package by.shplau.controllers;

import by.shplau.entities.Route;
import by.shplau.services.RouteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes(
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) Integer maxDuration) {
        List<Route> routes = routeService.getAllRoutes();
        if (difficulty != null) {
            routes.removeIf(route -> route.getDifficulty() > difficulty);
        }
        if (maxDuration != null) {
            routes.removeIf(route -> route.getDuration() > maxDuration);
        }
        routes.forEach(route -> {
            if (route.getImageUrl() != null) {
                route.setImageUrl("/img/samples/routes/" + route.getImageUrl());
            }
            if (route.getThumbnailUrl() != null) {
                route.setThumbnailUrl("/img/samples/routes/" + route.getThumbnailUrl());
            }
        });
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Optional<Route> routeOpt = routeService.findRouteById(id);
        if (routeOpt.isPresent()) {
            Route route = routeOpt.get();
            if (route.getImageUrl() != null) {
                route.setImageUrl("/img/samples/routes/" + route.getImageUrl());
            }
            if (route.getThumbnailUrl() != null) {
                route.setThumbnailUrl("/img/samples/routes/" + route.getThumbnailUrl());
            }
            return ResponseEntity.ok(route);
        }
        return ResponseEntity.notFound().build();
    }   

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestParam("file") MultipartFile file, @RequestParam("route") String routeJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Route route = mapper.readValue(routeJson, Route.class);
            return ResponseEntity.ok(routeService.createRoute(route, file));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid route data", e);
        }
    }
}
