package by.shplau.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Это класс для информации пользователей сайта.<br>
 * Соответствует <b>User</b> в базе данных
 * @author chewy_pegasus
 * @since 19.11.2024
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    public User(String username, String email, String password) {
        super();
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }

    public User() {
        this.setUsername("John Doe");
        this.setEmail("no email");
        this.setPassword("zachem...");
    }
}
