package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.RegistrationResponseDto;
import com.campuseventc.campus_event_crowd_monitor.model.Event;
import com.campuseventc.campus_event_crowd_monitor.model.Registration;
import com.campuseventc.campus_event_crowd_monitor.model.User;
import com.campuseventc.campus_event_crowd_monitor.repository.EventRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.RegistrationRepository;
import com.campuseventc.campus_event_crowd_monitor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               EventRepository eventRepository,
                               UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Registration registerStudent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalArgumentException("User is already registered for this event");
        }

        long registeredCount = registrationRepository.countByEventId(eventId);
        if (registeredCount >= event.getCapacity()) {
            throw new IllegalStateException("Event has reached its maximum venue registration capacity (" + event.getCapacity() + ")");
        }

        String shortCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String ticketCode = "TKT-" + event.getId() + "-" + user.getId() + "-" + shortCode;
        String qrPayload = "CECM:TICKET:" + ticketCode + ":EVENT:" + event.getId();

        Registration reg = new Registration(user, event, ticketCode, qrPayload);
        return registrationRepository.save(reg);
    }

    public List<RegistrationResponseDto> getStudentRegistrations(Long userId) {
        return registrationRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationResponseDto> getEventRegistrations(Long eventId) {
        return registrationRepository.findByEventId(eventId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void cancelRegistration(Long registrationId, Long userId) {
        Registration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!reg.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized to cancel this registration");
        }
        if (reg.isCheckedIn()) {
            throw new IllegalStateException("Cannot cancel registration after check-in");
        }
        registrationRepository.delete(reg);
    }

    public RegistrationResponseDto mapToDto(Registration reg) {
        RegistrationResponseDto dto = new RegistrationResponseDto();
        dto.setId(reg.getId());
        dto.setEventId(reg.getEvent().getId());
        dto.setEventName(reg.getEvent().getName());
        dto.setEventVenue(reg.getEvent().getVenue());
        dto.setEventDate(reg.getEvent().getEventDate());
        dto.setEventEndDate(reg.getEvent().getEndDate());
        dto.setEventStatus(reg.getEvent().getStatus());
        dto.setTicketCode(reg.getTicketCode());
        dto.setQrCodeData(reg.getQrCodeData());
        dto.setRegisteredAt(reg.getRegisteredAt());
        dto.setCheckedIn(reg.isCheckedIn());
        dto.setCheckedInAt(reg.getCheckedInAt());
        dto.setStudentName(reg.getUser().getName());
        dto.setStudentEmail(reg.getUser().getEmail());
        return dto;
    }
}
