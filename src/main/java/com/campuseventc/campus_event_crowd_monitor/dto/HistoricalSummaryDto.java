package com.campuseventc.campus_event_crowd_monitor.dto;

import java.util.List;

public class HistoricalSummaryDto {
    private String category;
    private long totalEvents;
    private double averageCapacity;
    private double averageTurnoutRate;
    private double averageOccupancyRate;
    private int estimatedTurnoutForNewEvent;

    public HistoricalSummaryDto() {}

    public HistoricalSummaryDto(String category, long totalEvents, double averageCapacity, double averageTurnoutRate, double averageOccupancyRate, int estimatedTurnoutForNewEvent) {
        this.category = category;
        this.totalEvents = totalEvents;
        this.averageCapacity = averageCapacity;
        this.averageTurnoutRate = averageTurnoutRate;
        this.averageOccupancyRate = averageOccupancyRate;
        this.estimatedTurnoutForNewEvent = estimatedTurnoutForNewEvent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public double getAverageCapacity() {
        return averageCapacity;
    }

    public void setAverageCapacity(double averageCapacity) {
        this.averageCapacity = averageCapacity;
    }

    public double getAverageTurnoutRate() {
        return averageTurnoutRate;
    }

    public void setAverageTurnoutRate(double averageTurnoutRate) {
        this.averageTurnoutRate = averageTurnoutRate;
    }

    public double getAverageOccupancyRate() {
        return averageOccupancyRate;
    }

    public void setAverageOccupancyRate(double averageOccupancyRate) {
        this.averageOccupancyRate = averageOccupancyRate;
    }

    public int getEstimatedTurnoutForNewEvent() {
        return estimatedTurnoutForNewEvent;
    }

    public void setEstimatedTurnoutForNewEvent(int estimatedTurnoutForNewEvent) {
        this.estimatedTurnoutForNewEvent = estimatedTurnoutForNewEvent;
    }
}
