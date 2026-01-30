package com.DisasterAlert.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private String issueType;   // Injured, Trapped, Food
    private String priority;    // HIGH, MEDIUM, LOW
    private String status;      // RAISED, IN_PROGRESS, USER_CONFIRMED, RESOLVED
    private LocalDateTime createdTime = LocalDateTime.now();

    private String currentLocation; // <-- NEW FIELD
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "disaster_id")
    private Disaster disaster;

    public HelpRequest() {}

    // Getters & Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public String getCurrentLocation() { return currentLocation; } // <-- NEW GETTER
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; } // <-- NEW SETTER

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Disaster getDisaster() { return disaster; }
    public void setDisaster(Disaster disaster) { this.disaster = disaster; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
