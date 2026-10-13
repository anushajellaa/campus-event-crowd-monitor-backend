package com.campuseventc.campus_event_crowd_monitor.dto;

import java.util.List;

public class EventAnalyticsDto {
    private Long eventId;
    private String eventName;
    private String venue;
    private int capacity;
    private long registeredCount;
    private long checkedInCount;
    private double turnoutRate; // (checkedIn / registered) * 100
    private double capacityOccupancyRate; // (checkedIn / capacity) * 100
    private Integer expectedTurnout;
    private String peakTimeSlot;
    private int peakHourCount;
    private List<HourlyCheckInDto> timeDistribution;
    private List<CheckInLogDto> recentCheckIns;

    public EventAnalyticsDto() {}

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

    public double getTurnoutRate() {
        return turnoutRate;
    }

    public void setTurnoutRate(double turnoutRate) {
        this.turnoutRate = turnoutRate;
    }

    public double getCapacityOccupancyRate() {
        return capacityOccupancyRate;
    }

    public void setCapacityOccupancyRate(double capacityOccupancyRate) {
        this.capacityOccupancyRate = capacityOccupancyRate;
    }

    public Integer getExpectedTurnout() {
        return expectedTurnout;
    }

    public void setExpectedTurnout(Integer expectedTurnout) {
        this.expectedTurnout = expectedTurnout;
    }

    public String getPeakTimeSlot() {
        return peakTimeSlot;
    }

    public void setPeakTimeSlot(String peakTimeSlot) {
        this.peakTimeSlot = peakTimeSlot;
    }

    public int getPeakHourCount() {
        return peakHourCount;
    }

    public void setPeakHourCount(int peakHourCount) {
        this.peakHourCount = peakHourCount;
    }

    public List<HourlyCheckInDto> getTimeDistribution() {
        return timeDistribution;
    }

    public void setTimeDistribution(List<HourlyCheckInDto> timeDistribution) {
        this.timeDistribution = timeDistribution;
    }

    public List<CheckInLogDto> getRecentCheckIns() {
        return recentCheckIns;
    }

    public void setRecentCheckIns(List<CheckInLogDto> recentCheckIns) {
        this.recentCheckIns = recentCheckIns;
    }
}
