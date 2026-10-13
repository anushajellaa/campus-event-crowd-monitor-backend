package com.campuseventc.campus_event_crowd_monitor.repository;

import com.campuseventc.campus_event_crowd_monitor.model.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {
    List<CheckInLog> findByEventIdOrderByCheckInTimeDesc(Long eventId);
    List<CheckInLog> findByEventIdAndCheckInTimeBetween(Long eventId, LocalDateTime start, LocalDateTime end);
    long countByEventId(Long eventId);
}
