package com.campuseventc.campus_event_crowd_monitor.controller;

import com.campuseventc.campus_event_crowd_monitor.dto.CrowdMetricsDto;
import com.campuseventc.campus_event_crowd_monitor.service.CrowdMetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crowd")
@CrossOrigin(origins = "*")
public class CrowdMetricsController {

    private final CrowdMetricsService crowdMetricsService;

    public CrowdMetricsController(CrowdMetricsService crowdMetricsService) {
        this.crowdMetricsService = crowdMetricsService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<CrowdMetricsDto>> getAllMetrics() {
        return ResponseEntity.ok(crowdMetricsService.getAllMetrics());
    }

    @GetMapping("/metrics/{eventId}")
    public ResponseEntity<CrowdMetricsDto> getMetricsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(crowdMetricsService.calculateMetricsForEventId(eventId));
    }
}
