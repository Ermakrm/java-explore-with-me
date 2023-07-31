package ru.practicum.request.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestListMapper;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PrivateRequestController {
    RequestService requestService;
    RequestListMapper requestListMapper;
    RequestMapper requestMapper;

    @GetMapping
    public List<RequestDto> getRequestById(@PathVariable Long userId) {
        log.info("getRequestById userId={}", userId);
        return requestListMapper.toRequestDtoList(requestService.getRequestsByUserId(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequestByUserId(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("saveRequestByUserId userId={},eventId={}", userId, eventId);
        return requestMapper.toRequestDto(requestService.saveRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequestById(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        log.info("cancelRequestById userId={};requestId={}", userId, requestId);
        return requestMapper.toRequestDto(
                requestService.cancelRequest(userId, requestId)
        );
    }
}
