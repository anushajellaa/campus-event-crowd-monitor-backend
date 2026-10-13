package com.campuseventc.campus_event_crowd_monitor.model;

public enum CrowdLevel {
    LOW("Low Crowd", "Normal", "Venue is comfortable", "#10b981"),
    MEDIUM("Moderate Crowd", "Caution", "Approaching capacity", "#f59e0b"),
    HIGH("High Crowd / Alert", "Alert", "Near or above capacity", "#ef4444");

    private final String label;
    private final String status;
    private final String description;
    private final String colorCode;

    CrowdLevel(String label, String status, String description, String colorCode) {
        this.label = label;
        this.status = status;
        this.description = description;
        this.colorCode = colorCode;
    }

    public String getLabel() {
        return label;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getColorCode() {
        return colorCode;
    }
}
