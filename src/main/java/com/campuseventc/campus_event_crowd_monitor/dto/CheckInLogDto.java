package com.campuseventc.campus_event_crowd_monitor.dto;

import java.time.LocalDateTime;

public class CheckInLogDto {
    private Long id;
    private String attendeeName;
    private String attendeeEmail;
    private String ticketCode;
    private String method;
    private LocalDateTime checkInTime;

    public CheckInLogDto() {}

    public CheckInLogDto(Long id, String attendeeName, String attendeeEmail, String ticketCode, String method, LocalDateTime checkInTime) {
        this.id = id;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
        this.ticketCode = ticketCode;
        this.method = method;
        this.checkInTime = checkInTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}
