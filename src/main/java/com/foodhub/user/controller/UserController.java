package com.foodhub.user.controller;

import com.foodhub.user.dto.AddressRequest;
import com.foodhub.user.dto.AuthResponse;
import com.foodhub.user.dto.LoginRequest;
import com.foodhub.user.dto.RegisterRequest;
import com.foodhub.user.entity.Address;
import com.foodhub.user.entity.User;
import com.foodhub.user.service.AddressService;
import com.foodhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AddressService addressService;

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            String result = userService.register(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/auth/profile")
    public ResponseEntity<User> getProfile(@RequestParam String email) {
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<User> approveUser(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(userService.approveUser(id, status));
    }

    // Address endpoints
    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<Address> addAddress(@PathVariable Long userId, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.addAddress(userId, request));
    }

    @PutMapping("/addresses/{addressId}/users/{userId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @PathVariable Long userId, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, userId, request));
    }

    @DeleteMapping("/addresses/{addressId}/users/{userId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId, @PathVariable Long userId) {
        addressService.deleteAddress(addressId, userId);
        return ResponseEntity.ok("Address deleted successfully");
    }

    @PutMapping("/addresses/{addressId}/users/{userId}/default")
    public ResponseEntity<Address> setDefaultAddress(@PathVariable Long addressId, @PathVariable Long userId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(addressId, userId));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
