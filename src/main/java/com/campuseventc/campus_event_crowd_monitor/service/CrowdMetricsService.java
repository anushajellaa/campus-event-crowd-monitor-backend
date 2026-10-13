package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.CrowdMetricsDto;
import com.campuseventc.campus_event_crowd_monitor.model.CrowdAlert;
import com.campuseventc.campus_event_crowd_monitor.model.CrowdLevel;
import com.campuseventc.campus_event_crowd_monitor.model.Event;
import com.campuseventc.campus_event_crowd_monitor.repository.CrowdAlertRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.EventRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrowdMetricsService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final CrowdAlertRepository crowdAlertRepository;

    public CrowdMetricsService(EventRepository eventRepository,
                               RegistrationRepository registrationRepository,
                               CrowdAlertRepository crowdAlertRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.crowdAlertRepository = crowdAlertRepository;
    }

    public CrowdMetricsDto calculateMetrics(Event event) {
        long registeredCount = registrationRepository.countByEventId(event.getId());
        long checkedInCount = registrationRepository.countByEventIdAndCheckedInTrue(event.getId());
        int capacity = event.getCapacity() > 0 ? event.getCapacity() : 1;

        double occupancyPercentage = Math.round(((double) checkedInCount / capacity * 100.0) * 10.0) / 10.0;
        double registrationPercentage = Math.round(((double) registeredCount / capacity * 100.0) * 10.0) / 10.0;

        CrowdLevel crowdLevel;
        boolean alertTriggered = false;
        String alertMessage = null;

        if (occupancyPercentage < 50.0) {
            crowdLevel = CrowdLevel.LOW;
        } else if (occupancyPercentage < 80.0) {
            crowdLevel = CrowdLevel.MEDIUM;
        } else {
            crowdLevel = CrowdLevel.HIGH;
            alertTriggered = true;
            alertMessage = String.format("High crowd alert for '%s' at '%s'! Occupancy reached %.1f%% (%d / %d capacity). Immediate action required.",
                    event.getName(), event.getVenue(), occupancyPercentage, checkedInCount, capacity);

            triggerAlertIfNeeded(event, crowdLevel, occupancyPercentage, (int) checkedInCount, capacity, alertMessage);
        }

        CrowdMetricsDto dto = new CrowdMetricsDto();
        dto.setEventId(event.getId());
        dto.setEventName(event.getName());
        dto.setVenue(event.getVenue());
        dto.setEventStatus(event.getStatus());
        dto.setCapacity(capacity);
        dto.setRegisteredCount(registeredCount);
        dto.setCheckedInCount(checkedInCount);
        dto.setOccupancyPercentage(occupancyPercentage);
        dto.setRegistrationPercentage(registrationPercentage);
        dto.setCrowdLevel(crowdLevel);
        dto.setStatusBadge(crowdLevel.getLabel());
        dto.setStatusDescription(crowdLevel.getDescription());
        dto.setColorCode(crowdLevel.getColorCode());
        dto.setAlertTriggered(alertTriggered);
        dto.setAlertMessage(alertMessage);
        dto.setExpectedTurnout(event.getExpectedTurnout());
        dto.setTimestamp(LocalDateTime.now());

        return dto;
    }

    public CrowdMetricsDto calculateMetricsForEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));
        return calculateMetrics(event);
    }

    public List<CrowdMetricsDto> getAllMetrics() {
        List<Event> events = eventRepository.findAllByOrderByEventDateAsc();
        List<CrowdMetricsDto> result = new ArrayList<>();
        for (Event ev : events) {
            result.add(calculateMetrics(ev));
        }
        return result;
    }

    private synchronized void triggerAlertIfNeeded(Event event, CrowdLevel level, double occupancy, int checkedIn, int capacity, String message) {
        List<CrowdAlert> existing = crowdAlertRepository.findByEventIdOrderByCreatedAtDesc(event.getId());
        boolean hasRecentUnresolved = existing.stream().anyMatch(a -> !a.isResolved());
        if (!hasRecentUnresolved) {
            CrowdAlert alert = new CrowdAlert(event, level, occupancy, checkedIn, capacity, message);
            crowdAlertRepository.save(alert);
        }
    }
}
