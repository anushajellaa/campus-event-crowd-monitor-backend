package com.campuseventc.campus_event_crowd_monitor.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckInRequest {
    @NotBlank(message = "Ticket code or QR code is required")
    private String code;

    private Long eventId;

    private String method; // QR_CODE or MANUAL

    public CheckInRequest() {}

    public CheckInRequest(String code, Long eventId, String method) {
        this.code = code;
        this.eventId = eventId;
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
