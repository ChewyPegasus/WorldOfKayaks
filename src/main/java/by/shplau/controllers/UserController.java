package by.shplau.controllers;

import by.shplau.entities.User;
import by.shplau.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        
        return user.map(u -> ResponseEntity.ok(new UserProfileResponse(u)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
        return ResponseEntity.ok(createdUser);
    }
}

class UserProfileResponse {
    private String username;
    private String email;

    public UserProfileResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Геттеры
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}
