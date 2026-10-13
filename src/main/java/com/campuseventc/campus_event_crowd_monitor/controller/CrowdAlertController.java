package com.campuseventc.campus_event_crowd_monitor.controller;

import com.campuseventc.campus_event_crowd_monitor.model.CrowdAlert;
import com.campuseventc.campus_event_crowd_monitor.service.CrowdAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class CrowdAlertController {

    private final CrowdAlertService crowdAlertService;

    public CrowdAlertController(CrowdAlertService crowdAlertService) {
        this.crowdAlertService = crowdAlertService;
    }

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getAllAlerts() {
        return ResponseEntity.ok(crowdAlertService.getAllAlerts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CrowdAlert>> getActiveAlerts() {
        return ResponseEntity.ok(crowdAlertService.getActiveAlerts());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CrowdAlert>> getAlertsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(crowdAlertService.getAlertsByEvent(eventId));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<?> resolveAlert(@PathVariable Long id) {
        try {
            CrowdAlert alert = crowdAlertService.resolveAlert(id);
            return ResponseEntity.ok(alert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
