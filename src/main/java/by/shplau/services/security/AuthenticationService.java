package by.shplau.services.security;

import by.shplau.dto.AuthenticationRequest;
import by.shplau.dto.AuthenticationResponse;
import by.shplau.dto.RegisterRequest;
import by.shplau.entities.util.Role;
import by.shplau.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import by.shplau.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            System.out.println("Attempting to register user: " + request.getUsername());
            
            // Проверяем, существует ли пользователь с таким именем или email
            if (repository.findByUsername(request.getUsername()).isPresent()) {
                System.err.println("Username already exists: " + request.getUsername());
                throw new RuntimeException("Username already exists");
            }
            if (request.getEmail() != null && repository.findByEmail(request.getEmail()).isPresent()) {
                System.err.println("Email already exists: " + request.getEmail());
                throw new RuntimeException("Email already exists");
            }

            var user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            
            System.out.println("Successfully registered user: " + request.getUsername());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            System.err.println("Registration error for user " + request.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            System.out.println("Attempting to authenticate user with login: " + request.getLogin());
            
            // Определяем, является ли login email-ом
            User user;
            if (request.getLogin().contains("@")) {
                System.out.println("Attempting email login");
                user = repository.findByEmail(request.getLogin())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            } else {
                System.out.println("Attempting username login");
                user = repository.findByUsername(request.getLogin())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }

            // Аутентифицируем пользователя
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);
            System.out.println("Successfully authenticated user: " + user.getUsername());
            
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
