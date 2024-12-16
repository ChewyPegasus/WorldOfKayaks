package by.shplau.controllers;

import by.shplau.entities.Route;
import by.shplau.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = "http://localhost:3000")
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

        // Add full image URL
        routes.forEach(route -> {
            if (route.getImageUrl() != null) {
                route.setImageUrl("http://localhost:8080" + route.getImageUrl());
            }
        });

        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        Optional<Route> routeOpt = routeService.findRouteById(id);
        return routeOpt.map(route -> {
            // Add full image URL
            if (route.getImageUrl() != null) {
                route.setImageUrl("http://localhost:8080" + route.getImageUrl());
            }
            return ResponseEntity.ok(route);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route createdRoute = routeService.createRoute(route);
        return ResponseEntity.ok(createdRoute);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = routeService.uploadImage(id, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading image: " + e.getMessage());
        }
    }
}
