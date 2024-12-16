package by.shplau.services;

import by.shplau.entities.Route;
import by.shplau.repositories.RouteRepository;
import by.shplau.services.FileStorageService;
import by.shplau.util.ImageUploadResult;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private FileStorageService fileService;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Route createRoute(Route route) {
        // Ensure a default image is set if no image is provided
        if (route.getImageUrl() == null || route.getImageUrl().isEmpty()) {
            route.setImageUrl("/img/default/route.jpg");
        }
        return routeRepository.save(route);
    }

    public List<Route> findRoutesByDifficulty(int difficulty) {
        return routeRepository.findByDifficulty(difficulty);
    }

    public List<Route> getRoutesByDuration(int maxDuration) {
        return routeRepository.findByDurationLessThanEqual(maxDuration);
    }

    public String uploadImage(Long routeId, MultipartFile file) throws Exception {
        Optional<Route> routeOpt = routeRepository.findById(routeId);
        if (routeOpt.isEmpty()) {
            throw new Exception("Route not found");
        }

        Route route = routeOpt.get();
        ImageUploadResult result = fileService.storeRouteImage(file);
        String imageUrl = "/img/samples/routes/" + result.getImageFileName();
        route.setImageUrl(imageUrl);
        routeRepository.save(route);
        
        return imageUrl;
    }
}
