package com.foodhub.user.service;

import com.foodhub.user.config.JwtUtil;
import com.foodhub.user.dto.AuthResponse;
import com.foodhub.user.dto.LoginRequest;
import com.foodhub.user.dto.RegisterRequest;
import com.foodhub.user.entity.Role;
import com.foodhub.user.entity.User;
import com.foodhub.user.exception.UserNotFoundException;
import com.foodhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        try {
            logger.info("Registration request received for email: {}, role: {}", request.getEmail(), request.getRole());
            
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return "Email already registered";
            }
            
            // Validate restaurant owner requirements
            if (request.getRole() == Role.RESTAURANT_OWNER) {
                logger.info("Validating restaurant owner fields - PAN: {}, FSSAI: {}", 
                    request.getPanCardNumber(), request.getFssaiLicenseNumber());
                if (request.getPanCardNumber() == null || request.getPanCardNumber().trim().isEmpty()) {
                    return "PAN card number is required for restaurant owners";
                }
                if (request.getFssaiLicenseNumber() == null || request.getFssaiLicenseNumber().trim().isEmpty()) {
                    return "FSSAI license number is required for restaurant owners";
                }
            }
            
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            
            if (request.getRole() == Role.RESTAURANT_OWNER) {
                user.setPanCardNumber(request.getPanCardNumber());
                user.setFssaiLicenseNumber(request.getFssaiLicenseNumber());
                user.setApprovalStatus("PENDING");
            } else {
                user.setApprovalStatus("APPROVED");
            }
            
            logger.info("Saving user to database...");
            userRepository.save(user);
            logger.info("User registered successfully: {}", user.getEmail());
            
            if (request.getRole() == Role.RESTAURANT_OWNER) {
                return "Registration successful. Your account is pending admin approval.";
            }
            return "User registered successfully";
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmailOrPhone());
        
        User user = userRepository.findByEmail(request.getEmailOrPhone())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Invalid password for email: {}", request.getEmailOrPhone());
            throw new RuntimeException("Invalid credentials");
        }
        
        if ("PENDING".equals(user.getApprovalStatus())) {
            throw new RuntimeException("Account pending admin approval");
        }
        
        if ("REJECTED".equals(user.getApprovalStatus())) {
            throw new RuntimeException("Account has been rejected");
        }
        
        String token = jwtUtil.generateToken(user.getEmail());
        logger.info("Token generated for user: {}, role: {}", user.getEmail(), user.getRole());
        
        return new AuthResponse("Login successful", user.getId(), user.getUsername(), user.getEmail(), user.getRole(), token);
    }

    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User approveUser(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setApprovalStatus(status);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        user.setPanCardNumber(userDetails.getPanCardNumber());
        user.setFssaiLicenseNumber(userDetails.getFssaiLicenseNumber());
        
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
