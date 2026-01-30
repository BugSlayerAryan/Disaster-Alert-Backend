package com.DisasterAlert.repository;

import com.DisasterAlert.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {

    // ✅ Get help requests by Clerk user ID (via User entity)
    List<HelpRequest> findAllByUser_ClerkUserId(String clerkUserId);
}
