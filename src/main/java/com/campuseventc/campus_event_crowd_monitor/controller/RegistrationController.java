package com.campuseventc.campus_event_crowd_monitor.controller;

import com.campuseventc.campus_event_crowd_monitor.dto.RegistrationResponseDto;
import com.campuseventc.campus_event_crowd_monitor.model.Registration;
import com.campuseventc.campus_event_crowd_monitor.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> registerForEvent(@RequestParam Long eventId, @RequestParam Long userId) {
        try {
            Registration reg = registrationService.registerStudent(eventId, userId);
            return ResponseEntity.ok(registrationService.mapToDto(reg));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<RegistrationResponseDto>> getMyRegistrations(@RequestParam Long userId) {
        return ResponseEntity.ok(registrationService.getStudentRegistrations(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<RegistrationResponseDto>> getEventRegistrations(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.getEventRegistrations(eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id, @RequestParam Long userId) {
        try {
            registrationService.cancelRegistration(id, userId);
            return ResponseEntity.ok(Map.of("message", "Registration cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
