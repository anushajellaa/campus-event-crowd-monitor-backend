package com.campuseventc.campus_event_crowd_monitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "crowd_alerts")
public class CrowdAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrowdLevel crowdLevel;

    @Column(nullable = false)
    private double occupancyPercentage;

    @Column(nullable = false)
    private int currentCheckedIn;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean resolved = false;

    private LocalDateTime resolvedAt;

    public CrowdAlert() {
    }

    public CrowdAlert(Event event, CrowdLevel crowdLevel, double occupancyPercentage, int currentCheckedIn, int capacity, String message) {
        this.event = event;
        this.crowdLevel = crowdLevel;
        this.occupancyPercentage = occupancyPercentage;
        this.currentCheckedIn = currentCheckedIn;
        this.capacity = capacity;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.resolved = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public CrowdLevel getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(CrowdLevel crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public double getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

    public int getCurrentCheckedIn() {
        return currentCheckedIn;
    }

    public void setCurrentCheckedIn(int currentCheckedIn) {
        this.currentCheckedIn = currentCheckedIn;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
