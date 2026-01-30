package com.DisasterAlert.controller;

import com.DisasterAlert.model.User;
import com.DisasterAlert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ===============================
    // 1️⃣ Sync user after Clerk login
    // ===============================
    @PostMapping("/sync")
    public ResponseEntity<User> syncUser(@RequestBody User userData) {

        Optional<User> existingUser =
                userRepository.findByClerkUserId(userData.getClerkUserId());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setName(userData.getName());
            user.setEmail(userData.getEmail());
        } else {
            user = new User();
            user.setClerkUserId(userData.getClerkUserId());
            user.setName(userData.getName());
            user.setEmail(userData.getEmail());
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // =====================================
    // 2️⃣ Update phone & location from popup
    // =====================================
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody User updateData) {

        Optional<User> optionalUser =
                userRepository.findByClerkUserId(updateData.getClerkUserId());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = optionalUser.get();

        // update only popup fields
        user.setPhone(updateData.getPhone());
        user.setLocation(updateData.getLocation());

        userRepository.save(user);

        return ResponseEntity.ok("Profile updated successfully");
    }

    // ===============================
    // 3️⃣ Get user by Clerk ID
    // ===============================
    @GetMapping("/{clerkUserId}")
    public ResponseEntity<User> getUser(@PathVariable String clerkUserId) {

        Optional<User> user =
                userRepository.findByClerkUserId(clerkUserId);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
