package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.mapper.EventListMapper;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminEventsController {
    EventService eventService;
    EventListMapper eventListMapper;
    EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getEvents users={};states{};categories{};start={};end={};from={};size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventListMapper.toEventFullDtoList(
                eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size)
        );
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest
    ) {
        log.info("updateEvent eventId={}; event={}", eventId, updateEventAdminRequest);
        return eventMapper.toEventFullDto(
                eventService.updateEvent(eventId, updateEventAdminRequest)
        );
    }
}
