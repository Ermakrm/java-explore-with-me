package ru.practicum.event.service;

import ru.practicum.enums.SortType;
import ru.practicum.enums.State;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event findEventById(Long id);

    Event findPublicEventById(Long id);

    List<Event> findEventsByUserId(Long userId, int from, int size);

    Event saveEvent(Event event, Long userId);

    Event findEventByEventAndUserIds(Long eventId, Long userId);

    Event updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<Event> getPublishedEvents(String text, List<Long> catIds, Boolean paid,
                                   LocalDateTime start, LocalDateTime end, Boolean onlyAvailable,
                                   SortType sortType, int from, int size);

    List<Event> getEvents(List<Long> userIds, List<State> states, List<Long> catIds,
                          LocalDateTime start, LocalDateTime end, int from, int size);

    Event updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    void existEventById(Long eventId);
}
