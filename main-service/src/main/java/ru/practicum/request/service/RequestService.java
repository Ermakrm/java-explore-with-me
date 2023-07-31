package ru.practicum.request.service;

import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> getRequestsByUserId(Long userId);

    List<Request> getRequestsByOwnerEvent(Long userId, Long eventId);

    Request saveRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    Request cancelRequest(Long userId, Long requestId);
}
