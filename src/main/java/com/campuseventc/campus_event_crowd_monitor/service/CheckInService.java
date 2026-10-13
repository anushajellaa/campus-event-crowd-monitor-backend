package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.CheckInRequest;
import com.campuseventc.campus_event_crowd_monitor.dto.CheckInResponse;
import com.campuseventc.campus_event_crowd_monitor.dto.CrowdMetricsDto;
import com.campuseventc.campus_event_crowd_monitor.model.CheckInLog;
import com.campuseventc.campus_event_crowd_monitor.model.Event;
import com.campuseventc.campus_event_crowd_monitor.model.Registration;
import com.campuseventc.campus_event_crowd_monitor.model.User;
import com.campuseventc.campus_event_crowd_monitor.repository.CheckInLogRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.EventRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.RegistrationRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CheckInService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final CheckInLogRepository checkInLogRepository;
    private final CrowdMetricsService crowdMetricsService;
    private final UserRepository userRepository;

    public CheckInService(RegistrationRepository registrationRepository,
                          EventRepository eventRepository,
                          CheckInLogRepository checkInLogRepository,
                          CrowdMetricsService crowdMetricsService,
                          UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.checkInLogRepository = checkInLogRepository;
        this.crowdMetricsService = crowdMetricsService;
        this.userRepository = userRepository;
    }

    @Transactional
    public CheckInResponse processCheckIn(CheckInRequest request) {
        String code = request.getCode().trim();
        String method = (request.getMethod() != null && !request.getMethod().isBlank())
                ? request.getMethod().toUpperCase()
                : "QR_CODE";

        // Parse ticket code if QR payload was scanned: "CECM:TICKET:TKT-...:EVENT:..."
        if (code.startsWith("CECM:TICKET:")) {
            String[] parts = code.split(":");
            if (parts.length >= 3) {
                code = parts[2];
            }
        }

        Optional<Registration> optReg = registrationRepository.findByTicketCode(code);
        if (optReg.isPresent()) {
            Registration reg = optReg.get();
            if (reg.isCheckedIn()) {
                CrowdMetricsDto metrics = crowdMetricsService.calculateMetrics(reg.getEvent());
                return new CheckInResponse(
                        false,
                        "Attendee " + reg.getUser().getName() + " was ALREADY checked in at " + reg.getCheckedInAt(),
                        reg.getTicketCode(),
                        reg.getUser().getName(),
                        reg.getUser().getEmail(),
                        reg.getEvent().getName(),
                        reg.getCheckedInAt(),
                        metrics
                );
            }

            LocalDateTime now = LocalDateTime.now();
            reg.setCheckedIn(true);
            reg.setCheckedInAt(now);
            reg.setCheckInMethod(method);
            registrationRepository.save(reg);

            CheckInLog log = new CheckInLog(
                    reg.getEvent(),
                    reg,
                    method,
                    reg.getUser().getName(),
                    reg.getUser().getEmail()
            );
            checkInLogRepository.save(log);

            CrowdMetricsDto metrics = crowdMetricsService.calculateMetrics(reg.getEvent());
            return new CheckInResponse(
                    true,
                    "Check-in successful! Welcome, " + reg.getUser().getName(),
                    reg.getTicketCode(),
                    reg.getUser().getName(),
                    reg.getUser().getEmail(),
                    reg.getEvent().getName(),
                    now,
                    metrics
            );
        }

        // If manual check-in by Email & Event ID:
        if (request.getEventId() != null && code.contains("@")) {
            Optional<User> userOpt = userRepository.findByEmail(code);
            if (userOpt.isPresent()) {
                Optional<Registration> regByEventUser = registrationRepository.findByEventIdAndUserId(request.getEventId(), userOpt.get().getId());
                if (regByEventUser.isPresent()) {
                    Registration reg = regByEventUser.get();
                    if (reg.isCheckedIn()) {
                        CrowdMetricsDto metrics = crowdMetricsService.calculateMetrics(reg.getEvent());
                        return new CheckInResponse(
                                false,
                                "Attendee was already checked in at " + reg.getCheckedInAt(),
                                reg.getTicketCode(),
                                reg.getUser().getName(),
                                reg.getUser().getEmail(),
                                reg.getEvent().getName(),
                                reg.getCheckedInAt(),
                                metrics
                        );
                    }
                    LocalDateTime now = LocalDateTime.now();
                    reg.setCheckedIn(true);
                    reg.setCheckedInAt(now);
                    reg.setCheckInMethod("MANUAL");
                    registrationRepository.save(reg);

                    CheckInLog log = new CheckInLog(reg.getEvent(), reg, "MANUAL", reg.getUser().getName(), reg.getUser().getEmail());
                    checkInLogRepository.save(log);

                    CrowdMetricsDto metrics = crowdMetricsService.calculateMetrics(reg.getEvent());
                    return new CheckInResponse(
                            true,
                            "Manual check-in verified for " + reg.getUser().getName(),
                            reg.getTicketCode(),
                            reg.getUser().getName(),
                            reg.getUser().getEmail(),
                            reg.getEvent().getName(),
                            now,
                            metrics
                    );
                }
            }
        }

        throw new IllegalArgumentException("No matching registration found for ticket or email: " + code);
    }

    @Transactional
    public CrowdMetricsDto simulateWalkInCheckIn(Long eventId, String attendeeName) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        String syntheticEmail = "walkin_" + System.currentTimeMillis() % 10000 + "@campus.edu";
        String name = (attendeeName != null && !attendeeName.isBlank()) ? attendeeName : "Walk-in Attendee";

        CheckInLog log = new CheckInLog(event, null, "WALK_IN_SIMULATION", name, syntheticEmail);
        checkInLogRepository.save(log);

        // Also create a synthetic registration record marked as checked-in for accurate counts
        User walkInUser = new User(name, syntheticEmail, "9999999999", "pass", com.campuseventc.campus_event_crowd_monitor.model.Role.STUDENT);
        walkInUser = userRepository.save(walkInUser);

        String ticket = "WLK-" + event.getId() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Registration reg = new Registration(walkInUser, event, ticket, "CECM:TICKET:" + ticket);
        reg.setCheckedIn(true);
        reg.setCheckedInAt(LocalDateTime.now());
        reg.setCheckInMethod("SIMULATED_WALK_IN");
        registrationRepository.save(reg);

        log.setRegistration(reg);
        checkInLogRepository.save(log);

        return crowdMetricsService.calculateMetrics(event);
    }
}
