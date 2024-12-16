package by.shplau.repositories;

import by.shplau.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {


    List<User> findAll();

    User findByUsername(String serge);

    User findByEmail(String mail);
}
