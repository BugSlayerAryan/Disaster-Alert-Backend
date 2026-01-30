package com.DisasterAlert.service;

import com.DisasterAlert.dto.UserDto;
import com.DisasterAlert.model.User;
import com.DisasterAlert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🔹 Sync user with DB (insert or update)
    public User syncUser(UserDto userDto) {
        if (userDto.getClerkUserId() == null || userDto.getClerkUserId().isEmpty()) {
            throw new IllegalArgumentException("clerkUserId cannot be null or empty");
        }

        Optional<User> existingUser = userRepository.findByClerkUserId(userDto.getClerkUserId());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setClerkUserId(userDto.getClerkUserId());
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setLocation(userDto.getLocation());
        user.setRole(userDto.getRole() != null ? userDto.getRole() : "ROLE_USER");

        return userRepository.save(user);
    }

    // 🔹 Save user if not exists (needed for JWT filter)
    public void saveUserIfNotExists(String clerkUserId, String email, String name, String role) {
        if (clerkUserId == null || clerkUserId.isEmpty()) return;

        // 🔹 Check if user exists by clerkUserId
        boolean exists = userRepository.findByClerkUserId(clerkUserId).isPresent();

        if (!exists) {
            User user = new User();
            user.setClerkUserId(clerkUserId);
            user.setEmail(email);
            user.setName(name);
            user.setRole(role != null ? role : "ROLE_USER");
            userRepository.save(user);
        }
    }

}
