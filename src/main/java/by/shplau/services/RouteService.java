package by.shplau.services;

import by.shplau.entities.Route;
import by.shplau.repositories.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    private final Path ROUTES_PATH = Paths.get("src/main/resources/static/img/samples/routes").toAbsolutePath().normalize();

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route createRoute(Route route, MultipartFile file) {
        // Ensure a default image is set if no image is provided
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (!Files.exists(ROUTES_PATH)) {
                    Files.createDirectories(ROUTES_PATH);
                }
                Files.copy(file.getInputStream(), ROUTES_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                route.setImageUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + fileName, e);
            }
        } else if (route.getImageUrl() == null || route.getImageUrl().isEmpty()) {
            // Set a default route image if no image is provided
            route.setImageUrl("route.jpg");
        }
        return routeRepository.save(route);
    }

    public Route updateRouteImage(Long id, Route route, MultipartFile image) {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such route to be updated"));

        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (existingRoute.getImageUrl() != null) {
                String oldFileName = existingRoute.getImageUrl().substring(
                        existingRoute.getImageUrl().lastIndexOf("/") + 1);
                try {
                    Files.deleteIfExists(ROUTES_PATH.resolve(oldFileName));
                } catch (IOException e) {
                    // Log error but continue
                    e.printStackTrace();
                }
            }

            // Save new image
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            try {
                Files.copy(image.getInputStream(), ROUTES_PATH.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                route.setImageUrl("img/samples/routes/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + fileName, e);
            }
        }

        // Update other fields
        existingRoute.setRiverName(route.getRiverName());
        existingRoute.setDescription(route.getDescription());
        existingRoute.setDifficulty(route.getDifficulty());
        existingRoute.setDuration(route.getDuration());
        if (route.getImageUrl() != null) {
            existingRoute.setImageUrl(route.getImageUrl());
        }

        return routeRepository.save(existingRoute);
    }

    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No route to be deleted"));

        // Delete associated image
        if (route.getImageUrl() != null) {
            String fileName = route.getImageUrl().substring(
                    route.getImageUrl().lastIndexOf("/") + 1);
            try {
                Files.deleteIfExists(ROUTES_PATH.resolve(fileName));
            } catch (IOException e) {
                // Log error but continue with route deletion
                e.printStackTrace();
            }
        }

        routeRepository.delete(route);
    }

    // Additional methods for filtering
    public List<Route> findRoutesByDifficulty(int difficulty) {
        return routeRepository.findByDifficulty(difficulty);
    }

    public List<Route> getRoutesByDuration(int maxDuration) {
        return routeRepository.findByDurationLessThanEqual(maxDuration);
    }
}
