package com.DisasterAlert.repository;

import com.DisasterAlert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Existing (unchanged)
    Optional<User> findByUsername(String username);

    // 🔹 NEW (for Clerk)
    Optional<User> findByClerkUserId(String clerkUserId);
}

