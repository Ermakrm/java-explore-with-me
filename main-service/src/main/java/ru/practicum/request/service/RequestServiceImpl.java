package ru.practicum.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.IllegalActionException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestListMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImpl implements RequestService {
    UserService userService;
    EventService eventService;
    RequestRepository requestRepository;
    RequestListMapper requestListMapper;

    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        userService.findUserById(userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    public List<Request> getRequestsByOwnerEvent(Long userId, Long eventId) {
        userService.existUserById(userId);
        eventService.existEventById(eventId);

        return requestRepository.findAllByEventId(eventId);
    }

    @Override
    public Request saveRequest(Long userId, Long eventId) {
        User initiator = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new IllegalActionException("You can't create request in your event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IllegalActionException("Event is not published yet");
        }

        Status status;
        if (event.getParticipantLimit() == 0) {
            status = Status.CONFIRMED;
        } else {
            int countRequests = requestRepository.countAllByEventId(eventId);

            if (countRequests == event.getParticipantLimit()) {
                throw new IllegalActionException("Limit reached");
            }

            status = Status.PENDING;
        }

        return requestRepository.save(Request.builder()
                .requester(initiator)
                .event(event)
                .created(LocalDateTime.now())
                .status(status)
                .build());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequests(Long userId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest request) {
        userService.existUserById(userId);
        eventService.existEventById(eventId);

        if (Objects.nonNull(request.getRequestIds())) {
            if (requestRepository.existsAllByStatusAndIdIn(Status.CONFIRMED, request.getRequestIds())) {
                throw new IllegalActionException("Cancelling confirmed requests is not allowed");
            }
        }

        requestRepository.updateRequestStatus(request.getStatus(), request.getRequestIds());

        List<RequestDto> confirmedRequests = requestListMapper.toRequestDtoList(
                requestRepository.findAllByEventIdAndStatus(eventId, Status.CONFIRMED)
        );

        List<RequestDto> rejectedRequests = requestListMapper.toRequestDtoList(
                requestRepository.findAllByEventIdAndStatus(eventId, Status.REJECTED)
        );

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    public Request cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.setStatus(Status.CANCELED);
        return requestRepository.save(request);
    }
}
