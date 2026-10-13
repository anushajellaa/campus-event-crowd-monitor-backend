package com.campuseventc.campus_event_crowd_monitor.dto;

import com.campuseventc.campus_event_crowd_monitor.model.CrowdLevel;
import com.campuseventc.campus_event_crowd_monitor.model.EventStatus;

import java.time.LocalDateTime;

public class CrowdMetricsDto {
    private Long eventId;
    private String eventName;
    private String venue;
    private EventStatus eventStatus;
    private int capacity;
    private long registeredCount;
    private long checkedInCount;
    private double occupancyPercentage; // checked-in vs capacity
    private double registrationPercentage; // registered vs capacity
    private CrowdLevel crowdLevel;
    private String statusBadge;
    private String statusDescription;
    private String colorCode;
    private boolean alertTriggered;
    private String alertMessage;
    private Integer expectedTurnout;
    private LocalDateTime timestamp;

    public CrowdMetricsDto() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(long registeredCount) {
        this.registeredCount = registeredCount;
    }

    public long getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(long checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public double getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

    public double getRegistrationPercentage() {
        return registrationPercentage;
    }

    public void setRegistrationPercentage(double registrationPercentage) {
        this.registrationPercentage = registrationPercentage;
    }

    public CrowdLevel getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(CrowdLevel crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public String getStatusBadge() {
        return statusBadge;
    }

    public void setStatusBadge(String statusBadge) {
        this.statusBadge = statusBadge;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isAlertTriggered() {
        return alertTriggered;
    }

    public void setAlertTriggered(boolean alertTriggered) {
        this.alertTriggered = alertTriggered;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public Integer getExpectedTurnout() {
        return expectedTurnout;
    }

    public void setExpectedTurnout(Integer expectedTurnout) {
        this.expectedTurnout = expectedTurnout;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
