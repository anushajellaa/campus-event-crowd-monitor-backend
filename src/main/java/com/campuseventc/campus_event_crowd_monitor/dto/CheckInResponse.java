package com.campuseventc.campus_event_crowd_monitor.dto;

import java.time.LocalDateTime;

public class CheckInResponse {
    private boolean success;
    private String message;
    private String ticketCode;
    private String attendeeName;
    private String attendeeEmail;
    private String eventName;
    private LocalDateTime checkedInAt;
    private CrowdMetricsDto crowdMetrics;

    public CheckInResponse() {}

    public CheckInResponse(boolean success, String message, String ticketCode, String attendeeName, String attendeeEmail, String eventName, LocalDateTime checkedInAt, CrowdMetricsDto crowdMetrics) {
        this.success = success;
        this.message = message;
        this.ticketCode = ticketCode;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
        this.eventName = eventName;
        this.checkedInAt = checkedInAt;
        this.crowdMetrics = crowdMetrics;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    public void setAttendeeEmail(String attendeeEmail) {
        this.attendeeEmail = attendeeEmail;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public CrowdMetricsDto getCrowdMetrics() {
        return crowdMetrics;
    }

    public void setCrowdMetrics(CrowdMetricsDto crowdMetrics) {
        this.crowdMetrics = crowdMetrics;
    }
}
