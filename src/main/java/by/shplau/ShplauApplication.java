package by.shplau;

import by.shplau.entities.User;
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

	//private static final Logger log = LoggerFactory.getLogger(ShplauApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShplauApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(UserRepository repository) {
//		return (args) -> {
//			// save a few customers
//			repository.save(new User("Jack", "mail1@mail.ru", "goida"));
//			repository.save(new User("Dave", "mail2@mail.ru", "meow"));
//			repository.save(new User("Nidel", "mail3@mail.ru", "sos"));
//			repository.save(new User("Serge", "mail4@mail.ru", "sos"));
//
//			// fetch all customers
//			log.info("Customers found with findAll():");
//			log.info("-------------------------------");
//			repository.findAll().forEach(customer -> {
//				log.info(customer.toString());
//			});
//			log.info("");
//
//			// fetch an individual customer by ID
//			User user = repository.findByUsername("Serge");
//			log.info("Customer found with findByUsername(\"Serge\"):");
//			log.info("--------------------------------");
//			log.info(user.toString());
//			log.info("");
//
//			// fetch customers by last name
//			log.info("Customer found with findByLastName('Bauer'):");
//			log.info("--------------------------------------------");
//			User user1 = repository.findByEmail("sosrepos@mail.ru");
//			log.info(user1.toString());
//			log.info("");
//		};
//	}

}

