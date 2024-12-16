package by.shplau;

import by.shplau.entities.Product;
import by.shplau.entities.Route;
import by.shplau.entities.User;
import by.shplau.entities.util.Point;
import by.shplau.repositories.ProductRepository;
import by.shplau.repositories.RouteRepository;
import by.shplau.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class ShplauApplication {

	private static final Logger log = LoggerFactory.getLogger(ShplauApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShplauApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoProducts(ProductRepository productRepository) {
		return (args) -> {
			Product product1 = new Product(
					"Каяк Pelican Sprint 100X",
					"Легкий каяк для начинающих",
					599.99,
					Product.Category.PRODUCT,
					"kayak1.jpg"
			);
			product1.setThumbnailURL("thumbnails/thumb_kayak1.jpg");

			Product product2 = new Product(
					"Инструктаж по технике безопасности",
					"Базовый курс безопасности на воде",
					49.99,
					Product.Category.SERVICE,
					"safety_course.jpg"
			);
			product2.setThumbnailURL("thumbnails/thumb_safety_course.jpg");

			Product product3 = new Product(
					"Спасательный жилет",
					"Профессиональный спасательный жилет",
					89.99,
					Product.Category.PRODUCT,
					"vest.jpg"
			);
			product3.setThumbnailURL("thumbnails/thumb_vest.jpg");

			productRepository.save(product1);
			productRepository.save(product2);
			productRepository.save(product3);
		};
	}

	@Bean
	public CommandLineRunner demoRoutes(RouteRepository routeRepository) {
		return (args) -> {
			Route route1 = new Route();
			route1.setRiverName("Днепр");
			route1.setDuration(120); // 2 часа
			route1.setDifficulty(2);
			route1.setPrice(79.99);
			route1.setImageUrl("dnepr_route.jpg");

			Route route2 = new Route();
			route2.setRiverName("Припять");
			route2.setDuration(180); // 3 часа
			route2.setDifficulty(3);
			route2.setPrice(99.99);
			route2.setImageUrl("pripyat_route.jpg");

			Route route3 = new Route();
			route3.setRiverName("Березина");
			route3.setDuration(90); // 1.5 часа
			route3.setDifficulty(1);
			route3.setPrice(59.99);
			route3.setImageUrl("berezina_route.jpg");

			// Сохраняем маршруты сначала, чтобы получить id
			routeRepository.save(route1);
			routeRepository.save(route2);
			routeRepository.save(route3);

			// Добавляем точки для первого маршрута
			route1.setPoints(List.of(
					new Point(53.9045, 27.5615, Point.Type.START, route1),
					new Point(53.9145, 27.5715, Point.Type.REST, route1),
					new Point(53.9245, 27.5815, Point.Type.FOOD, route1),
					new Point(53.9345, 27.5915, Point.Type.SIGHT, route1),
					new Point(53.9445, 27.6015, Point.Type.END, route1)
			));

			// Добавляем точки для второго маршрута
			route2.setPoints(List.of(
					new Point(52.0977, 27.7306, Point.Type.START, route2),
					new Point(52.1077, 27.7406, Point.Type.REST, route2),
					new Point(52.1177, 27.7506, Point.Type.SIGHT, route2),
					new Point(52.1277, 27.7606, Point.Type.END, route2)
			));

			// Добавляем точки для третьего маршрута
			route3.setPoints(List.of(
					new Point(53.1439, 29.2256, Point.Type.START, route3),
					new Point(53.1539, 29.2356, Point.Type.FOOD, route3),
					new Point(53.1639, 29.2456, Point.Type.END, route3)
			));

			// Сохраняем маршруты с точками
			routeRepository.save(route1);
			routeRepository.save(route2);
			routeRepository.save(route3);
		};
	}

	@Bean
	public CommandLineRunner demoUsers(UserRepository userRepository) {
		return (args) -> {
			User user1 = new User("john_doe", "john@example.com", "password123");
			User user2 = new User("jane_doe", "jane@example.com", "securepassword");
			User user3 = new User("admin", "admin@example.com", "adminpassword");

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
		};
	}
}

