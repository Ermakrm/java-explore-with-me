package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.SortType;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventListMapper;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.statistic.StatisticsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PublicEventsController {
    EventService eventService;
    EventMapper eventMapper;
    EventListMapper eventListMapper;
    StatisticsService statisticsService;

    @SneakyThrows
    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortType sortType,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        List<Event> events = eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sortType, from, size);
        log.info("getEvents text={};categories={};paid={};start={};end={};onlyAvailable={};sortType={};from={};size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortType, from, size);
        statisticsService.saveStatistics(request);
        return eventListMapper.toEventShortDtoList(events);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable Long id, HttpServletRequest request) {
        Event event = eventService.findPublicEventById(id);
        log.info("getEventById id={};event={}", id, event);
        statisticsService.saveStatistics(request);
        return eventMapper.toEventFullDto(event);
    }
}
