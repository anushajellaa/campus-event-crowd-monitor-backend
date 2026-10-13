package com.campuseventc.campus_event_crowd_monitor.config;

import com.campuseventc.campus_event_crowd_monitor.model.*;
import com.campuseventc.campus_event_crowd_monitor.repository.*;
import com.campuseventc.campus_event_crowd_monitor.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final CheckInLogRepository checkInLogRepository;
    private final CrowdAlertRepository crowdAlertRepository;

    public DataInitializer(UserRepository userRepository,
                           EventRepository eventRepository,
                           RegistrationRepository registrationRepository,
                           CheckInLogRepository checkInLogRepository,
                           CrowdAlertRepository crowdAlertRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.checkInLogRepository = checkInLogRepository;
        this.crowdAlertRepository = crowdAlertRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println(">>> Initializing Campus Event Crowd Monitor Seed Data...");

        // 1. Create Admin & Students
        String defaultHashedPass = AuthService.hashPassword("admin123");
        String studentHashedPass = AuthService.hashPassword("student123");

        User admin = new User("Campus Admin", "admin@campus.edu", "+1-555-0199", defaultHashedPass, Role.ADMIN);
        User student1 = new User("Priya Sharma", "priya@campus.edu", "+1-555-0101", studentHashedPass, Role.STUDENT);
        User student2 = new User("Alex Rivera", "alex@campus.edu", "+1-555-0102", studentHashedPass, Role.STUDENT);
        User student3 = new User("Rohan Verma", "rohan@campus.edu", "+1-555-0103", studentHashedPass, Role.STUDENT);
        User student4 = new User("Emily Chen", "emily@campus.edu", "+1-555-0104", studentHashedPass, Role.STUDENT);

        List<User> students = List.of(student1, student2, student3, student4);
        userRepository.save(admin);
        userRepository.saveAll(students);

        // Additional attendees for high crowd simulation
        List<User> extraStudents = new ArrayList<>();
        for (int i = 5; i <= 60; i++) {
            extraStudents.add(new User("Student " + i, "student" + i + "@campus.edu", "+1-555-0" + (100 + i), studentHashedPass, Role.STUDENT));
        }
        userRepository.saveAll(extraStudents);

        // 2. Create Events
        LocalDateTime now = LocalDateTime.now();

        // Event 1: High Crowd (Auditorium, Capacity: 50, ~43 checked in -> 86% occupancy)
        Event hackathon = new Event(
                "Hackathon & AI Showcase 2026",
                "Flagship annual 36-hour hackathon featuring cutting-edge generative AI, robotics, and cloud projects.",
                "Main Auditorium",
                now.minusHours(4),
                now.plusHours(8),
                50,
                "Technology",
                EventStatus.LIVE,
                45
        );

        // Event 2: Moderate Crowd (Amphitheater, Capacity: 60, ~36 checked in -> 60% occupancy)
        Event rockFest = new Event(
                "Campus Spring Music & Cultural Fest",
                "Live rock performances, dance troupes, and culinary stalls organized by the Student Cultural Guild.",
                "Open Amphitheater",
                now.minusHours(2),
                now.plusHours(5),
                60,
                "Cultural",
                EventStatus.LIVE,
                48
        );

        // Event 3: Low Crowd (Seminar Hall, Capacity: 40, ~14 checked in -> 35% occupancy)
        Event droneWorkshop = new Event(
                "Autonomous Drones Hands-on Workshop",
                "Interactive workshop covering flight controller programming, computer vision, and ROS2 fundamentals.",
                "Tech Hub Seminar Hall",
                now.minusHours(1),
                now.plusHours(3),
                40,
                "Workshop",
                EventStatus.LIVE,
                25
        );

        // Event 4: Upcoming Event
        Event careerFair = new Event(
                "Annual Career & Tech Internship Expo",
                "Connect with leading technology companies, alumni founders, and startup incubators for summer internships.",
                "Sports Arena",
                now.plusDays(3),
                now.plusDays(3).plusHours(6),
                300,
                "Career",
                EventStatus.UPCOMING,
                260
        );

        // Event 5: Upcoming Sports Event
        Event basketball = new Event(
                "Inter-Department Basketball Championship",
                "Championship finals between Computer Science and Mechanical Engineering.",
                "Indoor Stadium",
                now.plusDays(5),
                now.plusDays(5).plusHours(4),
                200,
                "Sports",
                EventStatus.UPCOMING,
                170
        );

        eventRepository.saveAll(List.of(hackathon, rockFest, droneWorkshop, careerFair, basketball));

        // 3. Register students & generate check-ins for Hackathon (to simulate High Crowd > 80%)
        int hackathonCheckedInCount = 43; // 43 / 50 = 86%
        for (int i = 0; i < 48; i++) {
            User u = (i < students.size()) ? students.get(i) : extraStudents.get(i - students.size());
            String tkt = "TKT-HK-" + (1000 + i);
            Registration reg = new Registration(u, hackathon, tkt, "CECM:TICKET:" + tkt + ":EVENT:" + hackathon.getId());
            if (i < hackathonCheckedInCount) {
                reg.setCheckedIn(true);
                LocalDateTime checkInTime = now.minusHours(3).plusMinutes(i * 4);
                reg.setCheckedInAt(checkInTime);
                reg.setCheckInMethod(i % 3 == 0 ? "MANUAL" : "QR_CODE");
                registrationRepository.save(reg);

                CheckInLog log = new CheckInLog(hackathon, reg, reg.getCheckInMethod(), u.getName(), u.getEmail());
                log.setCheckInTime(checkInTime);
                checkInLogRepository.save(log);
            } else {
                registrationRepository.save(reg);
            }
        }

        // Trigger Alert for Hackathon
        CrowdAlert alert = new CrowdAlert(
                hackathon,
                CrowdLevel.HIGH,
                86.0,
                hackathonCheckedInCount,
                hackathon.getCapacity(),
                "High crowd alert for 'Hackathon & AI Showcase 2026' at 'Main Auditorium'! Occupancy reached 86.0% (43 / 50 capacity). Recommend opening balcony seating."
        );
        crowdAlertRepository.save(alert);

        // 4. Register students & check-ins for Rock Fest (Moderate crowd: 36 / 60 = 60%)
        for (int i = 0; i < 40; i++) {
            User u = extraStudents.get(i % extraStudents.size());
            String tkt = "TKT-RF-" + (2000 + i);
            Registration reg = new Registration(u, rockFest, tkt, "CECM:TICKET:" + tkt + ":EVENT:" + rockFest.getId());
            if (i < 36) {
                reg.setCheckedIn(true);
                LocalDateTime checkInTime = now.minusHours(2).plusMinutes(i * 3);
                reg.setCheckedInAt(checkInTime);
                reg.setCheckInMethod("QR_CODE");
                registrationRepository.save(reg);

                CheckInLog log = new CheckInLog(rockFest, reg, "QR_CODE", u.getName(), u.getEmail());
                log.setCheckInTime(checkInTime);
                checkInLogRepository.save(log);
            } else {
                registrationRepository.save(reg);
            }
        }

        // 5. Register students & check-ins for Drone Workshop (Low crowd: 14 / 40 = 35%)
        for (int i = 0; i < 20; i++) {
            User u = extraStudents.get(i % extraStudents.size());
            String tkt = "TKT-DW-" + (3000 + i);
            Registration reg = new Registration(u, droneWorkshop, tkt, "CECM:TICKET:" + tkt + ":EVENT:" + droneWorkshop.getId());
            if (i < 14) {
                reg.setCheckedIn(true);
                LocalDateTime checkInTime = now.minusMinutes(45).plusMinutes(i * 2);
                reg.setCheckedInAt(checkInTime);
                reg.setCheckInMethod("QR_CODE");
                registrationRepository.save(reg);

                CheckInLog log = new CheckInLog(droneWorkshop, reg, "QR_CODE", u.getName(), u.getEmail());
                log.setCheckInTime(checkInTime);
                checkInLogRepository.save(log);
            } else {
                registrationRepository.save(reg);
            }
        }

        // 6. Registrations for upcoming Career Fair
        for (int i = 0; i < 15; i++) {
            User u = (i < students.size()) ? students.get(i) : extraStudents.get(i);
            String tkt = "TKT-CF-" + (4000 + i);
            Registration reg = new Registration(u, careerFair, tkt, "CECM:TICKET:" + tkt + ":EVENT:" + careerFair.getId());
            registrationRepository.save(reg);
        }

        System.out.println(">>> Campus Event Crowd Monitor Seed Data successfully populated!");
    }
}
