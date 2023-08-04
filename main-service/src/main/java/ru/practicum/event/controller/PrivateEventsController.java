package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventListMapper;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestListMapper;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PrivateEventsController {
    EventService eventService;
    EventListMapper eventListMapper;
    EventMapper eventMapper;
    RequestService requestService;
    RequestListMapper requestListMapper;
    CommentService commentService;
    CommentMapper commentMapper;

    @GetMapping
    public List<EventShortDto> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getEvents userId={};from={};size{}", userId, from, size);
        return eventListMapper.toEventShortDtoList(
                eventService.findEventsByUserId(userId, from, size)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(
            @PathVariable Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        Event event = eventMapper.toEvent(newEventDto);
        log.info("saveEvent userId={};event={}", userId, event);
        return eventMapper.toEventFullDto(
                eventService.saveEvent(event, userId)
        );
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsByEventAndUserIds(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        Event event = eventService.findEventByEventAndUserIds(eventId, userId);
        log.info("getEventsByEventAndUserIds userId={};eventId={};event={}", userId, eventId, event);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest
    ) {
        log.info("updateEvent userId={};eventId={};event={}", userId, eventId, updateEventUserRequest);
        return eventMapper.toEventFullDto(
                eventService.updateEvent(userId, eventId, updateEventUserRequest)
        );
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("getRequests userId={};eventId{}", userId, eventId);
        return requestListMapper.toRequestDtoList(
                requestService.getRequestsByOwnerEvent(userId, eventId)
        );
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        log.info("updateStatusByEventId userId={};eventId={};event={}", userId, eventId, eventRequestStatusUpdateRequest);
        return requestService.updateStatusRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto saveComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody CommentRequestDto commentRequestDto
            ) {
        log.info("saveComment userId={};eventId{};comment={}", userId, eventId, commentRequestDto);
        Comment comment = commentService.saveComment(
                commentMapper.toComment(commentRequestDto), userId, eventId
        );
        return commentMapper.toCommentResponseDto(comment);
    }
}