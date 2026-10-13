package com.campuseventc.campus_event_crowd_monitor.controller;

import com.campuseventc.campus_event_crowd_monitor.dto.CheckInRequest;
import com.campuseventc.campus_event_crowd_monitor.dto.CheckInResponse;
import com.campuseventc.campus_event_crowd_monitor.dto.CrowdMetricsDto;
import com.campuseventc.campus_event_crowd_monitor.service.CheckInService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
@CrossOrigin(origins = "*")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ResponseEntity<?> checkIn(@Valid @RequestBody CheckInRequest request) {
        try {
            CheckInResponse response = checkInService.processCheckIn(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/simulate-walkin")
    public ResponseEntity<?> simulateWalkIn(@RequestParam Long eventId, @RequestParam(required = false) String attendeeName) {
        try {
            CrowdMetricsDto metrics = checkInService.simulateWalkInCheckIn(eventId, attendeeName);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
