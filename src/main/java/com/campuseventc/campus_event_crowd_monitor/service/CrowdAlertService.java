package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.model.CrowdAlert;
import com.campuseventc.campus_event_crowd_monitor.repository.CrowdAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrowdAlertService {

    private final CrowdAlertRepository crowdAlertRepository;

    public CrowdAlertService(CrowdAlertRepository crowdAlertRepository) {
        this.crowdAlertRepository = crowdAlertRepository;
    }

    public List<CrowdAlert> getActiveAlerts() {
        return crowdAlertRepository.findByResolvedFalseOrderByCreatedAtDesc();
    }

    public List<CrowdAlert> getAllAlerts() {
        return crowdAlertRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<CrowdAlert> getAlertsByEvent(Long eventId) {
        return crowdAlertRepository.findByEventIdOrderByCreatedAtDesc(eventId);
    }

    public CrowdAlert resolveAlert(Long alertId) {
        CrowdAlert alert = crowdAlertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with id: " + alertId));
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        return crowdAlertRepository.save(alert);
    }

    public long getActiveAlertsCount() {
        return crowdAlertRepository.countByResolvedFalse();
    }
}
