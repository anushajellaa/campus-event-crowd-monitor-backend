package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.AuthRequest;
import com.campuseventc.campus_event_crowd_monitor.dto.AuthResponse;
import com.campuseventc.campus_event_crowd_monitor.dto.RegisterRequest;
import com.campuseventc.campus_event_crowd_monitor.model.Role;
import com.campuseventc.campus_event_crowd_monitor.model.User;
import com.campuseventc.campus_event_crowd_monitor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }

        String hashedPassword = hashPassword(req.getPassword());
        User user = new User(
                req.getName().trim(),
                req.getEmail().trim().toLowerCase(),
                req.getMobile().trim(),
                hashedPassword,
                req.getRole() != null ? req.getRole() : Role.STUDENT
        );

        User saved = userRepository.save(user);
        String token = generateToken(saved);

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getMobile(),
                saved.getRole(),
                "Registration successful"
        );
    }

    public AuthResponse login(AuthRequest req) {
        String email = req.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String hashedInput = hashPassword(req.getPassword());
        if (!user.getPassword().equals(hashedInput)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = generateToken(user);
        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobile(),
                user.getRole(),
                "Login successful"
        );
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    private String generateToken(User user) {
        String payload = user.getId() + ":" + user.getEmail() + ":" + user.getRole() + ":" + UUID.randomUUID();
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
