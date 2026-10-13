package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.CheckInLogDto;
import com.campuseventc.campus_event_crowd_monitor.dto.EventAnalyticsDto;
import com.campuseventc.campus_event_crowd_monitor.dto.HistoricalSummaryDto;
import com.campuseventc.campus_event_crowd_monitor.dto.HourlyCheckInDto;
import com.campuseventc.campus_event_crowd_monitor.model.CheckInLog;
import com.campuseventc.campus_event_crowd_monitor.model.Event;
import com.campuseventc.campus_event_crowd_monitor.repository.CheckInLogRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.EventRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final CheckInLogRepository checkInLogRepository;

    public AnalyticsService(EventRepository eventRepository,
                            RegistrationRepository registrationRepository,
                            CheckInLogRepository checkInLogRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.checkInLogRepository = checkInLogRepository;
    }

    public EventAnalyticsDto getEventAnalytics(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        long registeredCount = registrationRepository.countByEventId(eventId);
        long checkedInCount = registrationRepository.countByEventIdAndCheckedInTrue(eventId);
        int capacity = event.getCapacity() > 0 ? event.getCapacity() : 1;

        double turnoutRate = registeredCount > 0
                ? Math.round(((double) checkedInCount / registeredCount * 100.0) * 10.0) / 10.0
                : 0.0;
        double capacityOccupancyRate = Math.round(((double) checkedInCount / capacity * 100.0) * 10.0) / 10.0;

        List<CheckInLog> logs = checkInLogRepository.findByEventIdOrderByCheckInTimeDesc(eventId);

        // Group check-ins by hour
        Map<String, Integer> hourlyCounts = new TreeMap<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:00");

        for (CheckInLog log : logs) {
            String slot = log.getCheckInTime().format(timeFormatter);
            hourlyCounts.put(slot, hourlyCounts.getOrDefault(slot, 0) + 1);
        }

        // If no logs, synthesize default slots based on event date
        if (hourlyCounts.isEmpty()) {
            String baseSlot = event.getEventDate().format(timeFormatter);
            hourlyCounts.put(baseSlot, 0);
        }

        List<HourlyCheckInDto> timeDistribution = new ArrayList<>();
        int runningTotal = 0;
        String peakSlot = "-";
        int maxPeak = 0;

        for (Map.Entry<String, Integer> entry : hourlyCounts.entrySet()) {
            runningTotal += entry.getValue();
            timeDistribution.add(new HourlyCheckInDto(entry.getKey(), entry.getValue(), runningTotal));
            if (entry.getValue() > maxPeak) {
                maxPeak = entry.getValue();
                peakSlot = entry.getKey();
            }
        }

        List<CheckInLogDto> recentLogs = logs.stream()
                .limit(20)
                .map(l -> new CheckInLogDto(
                        l.getId(),
                        l.getAttendeeName(),
                        l.getAttendeeEmail(),
                        l.getRegistration() != null ? l.getRegistration().getTicketCode() : "WALK-IN",
                        l.getMethod(),
                        l.getCheckInTime()
                ))
                .collect(Collectors.toList());

        EventAnalyticsDto dto = new EventAnalyticsDto();
        dto.setEventId(event.getId());
        dto.setEventName(event.getName());
        dto.setVenue(event.getVenue());
        dto.setCapacity(capacity);
        dto.setRegisteredCount(registeredCount);
        dto.setCheckedInCount(checkedInCount);
        dto.setTurnoutRate(turnoutRate);
        dto.setCapacityOccupancyRate(capacityOccupancyRate);
        dto.setExpectedTurnout(event.getExpectedTurnout());
        dto.setPeakTimeSlot(peakSlot);
        dto.setPeakHourCount(maxPeak);
        dto.setTimeDistribution(timeDistribution);
        dto.setRecentCheckIns(recentLogs);

        return dto;
    }

    public List<HistoricalSummaryDto> getHistoricalCategoryAverages() {
        List<Event> allEvents = eventRepository.findAll();
        Map<String, List<Event>> categoryMap = allEvents.stream()
                .collect(Collectors.groupingBy(e -> e.getCategory() != null ? e.getCategory() : "General"));

        List<HistoricalSummaryDto> summaries = new ArrayList<>();
        for (Map.Entry<String, List<Event>> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            List<Event> list = entry.getValue();
            long count = list.size();
            double totalCapacity = 0;
            double totalTurnoutRate = 0;
            double totalOccupancyRate = 0;

            for (Event ev : list) {
                totalCapacity += ev.getCapacity();
                long reg = registrationRepository.countByEventId(ev.getId());
                long chk = registrationRepository.countByEventIdAndCheckedInTrue(ev.getId());
                double turnout = reg > 0 ? ((double) chk / reg * 100.0) : 75.0;
                double occ = ev.getCapacity() > 0 ? ((double) chk / ev.getCapacity() * 100.0) : 60.0;
                totalTurnoutRate += turnout;
                totalOccupancyRate += occ;
            }

            double avgCapacity = count > 0 ? Math.round(totalCapacity / count) : 100;
            double avgTurnout = count > 0 ? Math.round(totalTurnoutRate / count * 10.0) / 10.0 : 78.5;
            double avgOccupancy = count > 0 ? Math.round(totalOccupancyRate / count * 10.0) / 10.0 : 68.2;
            int estimatedForNew = (int) Math.round(avgCapacity * (avgOccupancy / 100.0));

            summaries.add(new HistoricalSummaryDto(category, count, avgCapacity, avgTurnout, avgOccupancy, estimatedForNew));
        }

        return summaries;
    }
}
