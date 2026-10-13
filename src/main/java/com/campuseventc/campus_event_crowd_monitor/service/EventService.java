package com.campuseventc.campus_event_crowd_monitor.service;

import com.campuseventc.campus_event_crowd_monitor.dto.EventRequestDto;
import com.campuseventc.campus_event_crowd_monitor.model.Event;
import com.campuseventc.campus_event_crowd_monitor.model.EventStatus;
import com.campuseventc.campus_event_crowd_monitor.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc();
    }

    public List<Event> getEventsByStatus(EventStatus status) {
        return eventRepository.findByStatus(status);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
    }

    public Event createEvent(EventRequestDto dto) {
        Event event = new Event();
        event.setName(dto.getName().trim());
        event.setDescription(dto.getDescription());
        event.setVenue(dto.getVenue().trim());
        event.setEventDate(dto.getEventDate());
        event.setEndDate(dto.getEndDate());
        event.setCapacity(dto.getCapacity());
        event.setCategory(dto.getCategory() != null ? dto.getCategory().trim() : "General");
        event.setStatus(dto.getStatus() != null ? dto.getStatus() : EventStatus.UPCOMING);

        // Historical estimation fallback if not provided: ~75% of capacity based on campus benchmark
        if (dto.getExpectedTurnout() != null && dto.getExpectedTurnout() > 0) {
            event.setExpectedTurnout(dto.getExpectedTurnout());
        } else {
            event.setExpectedTurnout((int) Math.round(dto.getCapacity() * 0.75));
        }

        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, EventRequestDto dto) {
        Event event = getEventById(id);
        event.setName(dto.getName().trim());
        event.setDescription(dto.getDescription());
        event.setVenue(dto.getVenue().trim());
        event.setEventDate(dto.getEventDate());
        event.setEndDate(dto.getEndDate());
        event.setCapacity(dto.getCapacity());
        if (dto.getCategory() != null) {
            event.setCategory(dto.getCategory().trim());
        }
        if (dto.getStatus() != null) {
            event.setStatus(dto.getStatus());
        }
        if (dto.getExpectedTurnout() != null) {
            event.setExpectedTurnout(dto.getExpectedTurnout());
        }

        return eventRepository.save(event);
    }

    public Event updateStatus(Long id, EventStatus status) {
        Event event = getEventById(id);
        event.setStatus(status);
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }
}
