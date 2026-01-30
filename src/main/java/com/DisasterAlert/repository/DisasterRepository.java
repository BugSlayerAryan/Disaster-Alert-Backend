package com.DisasterAlert.repository;


import com.DisasterAlert.model.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterRepository extends JpaRepository<Disaster, Long> {
}
