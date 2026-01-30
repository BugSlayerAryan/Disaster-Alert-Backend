package com.DisasterAlert.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Disaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disasterId;

    private String type;
    private String location;
    private String severity; // LOW, MEDIUM, HIGH
    private String status;   // ACTIVE, RESOLVED
    private String source;   // MANUAL / GOVT_API
    private LocalDateTime createdTime = LocalDateTime.now();

    // Default constructor
    public Disaster() {}

    // Getters & Setters
    public Long getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(Long disasterId) {
        this.disasterId = disasterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
