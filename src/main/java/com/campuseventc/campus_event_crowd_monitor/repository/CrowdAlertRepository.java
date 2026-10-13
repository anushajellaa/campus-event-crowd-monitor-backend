package com.campuseventc.campus_event_crowd_monitor.repository;

import com.campuseventc.campus_event_crowd_monitor.model.CrowdAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrowdAlertRepository extends JpaRepository<CrowdAlert, Long> {
    List<CrowdAlert> findByEventIdOrderByCreatedAtDesc(Long eventId);
    List<CrowdAlert> findByResolvedFalseOrderByCreatedAtDesc();
    List<CrowdAlert> findAllByOrderByCreatedAtDesc();
    long countByResolvedFalse();
}
