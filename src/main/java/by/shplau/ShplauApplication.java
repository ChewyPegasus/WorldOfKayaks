package by.shplau;

import by.shplau.entities.Product;
import by.shplau.entities.User;
import by.shplau.repositories.ProductRepository;
import by.shplau.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class ShplauApplication {

	private static final Logger log = LoggerFactory.getLogger(ShplauApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShplauApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(ProductRepository repository) {
//		return (args) -> {
//			// save a few customers
//			repository.save(new Product("kayak", "comfortable", 123.456, Product.Category.PRODUCT, ));
//			repository.save(new User("Dave", "mail2@mail.ru", "meow"));
//			repository.save(new User("Nidel", "mail3@mail.ru", "sos"));
//			repository.save(new User("Serge", "mail4@mail.ru", "sos"));
//		};
//	}

}

