package com.DisasterAlert.repository;



import com.DisasterAlert.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Add this method for Spring Data JPA
    Optional<Admin> findByUsername(String username);
}
