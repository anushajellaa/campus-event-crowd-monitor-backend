package com.campuseventc.campus_event_crowd_monitor.dto;

public class HourlyCheckInDto {
    private String timeSlot;
    private int checkInCount;
    private int cumulativeCount;

    public HourlyCheckInDto() {}

    public HourlyCheckInDto(String timeSlot, int checkInCount, int cumulativeCount) {
        this.timeSlot = timeSlot;
        this.checkInCount = checkInCount;
        this.cumulativeCount = cumulativeCount;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public int getCumulativeCount() {
        return cumulativeCount;
    }

    public void setCumulativeCount(int cumulativeCount) {
        this.cumulativeCount = cumulativeCount;
    }
}
