package com.campuseventc.campus_event_crowd_monitor.controller;

import com.campuseventc.campus_event_crowd_monitor.dto.EventAnalyticsDto;
import com.campuseventc.campus_event_crowd_monitor.dto.HistoricalSummaryDto;
import com.campuseventc.campus_event_crowd_monitor.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventAnalyticsDto> getEventAnalytics(@PathVariable Long eventId) {
        return ResponseEntity.ok(analyticsService.getEventAnalytics(eventId));
    }

    @GetMapping("/historical")
    public ResponseEntity<List<HistoricalSummaryDto>> getHistoricalAverages() {
        return ResponseEntity.ok(analyticsService.getHistoricalCategoryAverages());
    }
}
