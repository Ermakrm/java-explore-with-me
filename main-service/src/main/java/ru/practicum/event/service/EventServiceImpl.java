package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.StatsDto;
import ru.practicum.enums.SortType;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.Status;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IllegalActionException;
import ru.practicum.exception.IllegalParametersException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.location.service.LocationService;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.statistic.StatisticsService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    EventMapper eventMapper;
    UserService userService;
    CategoryService categoryService;
    LocationService locationService;
    StatisticsService statisticsService;
    RequestRepository requestRepository;

    @Override
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%d not found", id)));
    }

    @Override
    @Transactional
    public Event findPublicEventById(Long id) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%d not found", id)));
        return addStatistics(event, null, null);
    }

    @Override
    public List<Event> findEventsByUserId(Long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));
    }

    @Override
    public Event findEventByEventAndUserIds(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId);
    }

    @Override
    public Event saveEvent(Event event, Long userId) {
        User user = userService.findUserById(userId);
        try {
            Category category = categoryService.findCategoryById(event.getId());
            event.setCategory(category);
        } catch (Exception ignore) {

        }
        event.setInitiator(user);
        locationService.findOrSaveLocation(event.getLocation());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        event.setConfirmedRequests(0L);
        event.setViews(0L);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userService.existUserById(userId);
        Event event = findEventById(eventId);

        if (event.getState().equals(State.PUBLISHED)) {
            throw new IllegalActionException("Can't change, because it already Published.");
        }
        if (updateEventUserRequest.getLocation() != null) {
            locationService.findOrSaveLocation(updateEventUserRequest.getLocation());
        }

        eventMapper.updateEvent(updateEventUserRequest, event);
        StateAction state = updateEventUserRequest.getStateAction();

        if (Objects.nonNull(updateEventUserRequest.getStateAction())) {

            switch (state) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEventById(eventId);
        eventMapper.updateEvent(updateEventAdminRequest, event);

        if (Objects.nonNull(updateEventAdminRequest.getLocation())) {
            locationService.findOrSaveLocation(updateEventAdminRequest.getLocation());
        }

        StateAction action = updateEventAdminRequest.getStateAction();
        State state = event.getState();
        if (Objects.nonNull(action)) {

            switch (action) {
                case PUBLISH_EVENT:
                    if (!state.equals(State.PENDING)) {
                        throw new IllegalActionException("Event is not Pending");
                    }
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(State.PUBLISHED);
                    break;

                case REJECT_EVENT:
                    if (state.equals(State.PUBLISHED)) {
                        throw new IllegalActionException("Event is not Published yet");
                    }
                    event.setState(State.CANCELED);
                    break;
            }
        }

        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public List<Event> getEvents(List<Long> userIds, List<State> states, List<Long> catIds,
                                 LocalDateTime start, LocalDateTime end, int from, int size) {

        QEvent event = QEvent.event;
        BooleanExpression bySearchCriteria = event.isNotNull();

        if (Objects.nonNull(userIds)) {
            bySearchCriteria = bySearchCriteria.and(event.initiator.id.in(userIds));
        }
        if (Objects.nonNull(states)) {
            bySearchCriteria = bySearchCriteria.and(event.state.in(states));
        }
        if (Objects.nonNull(catIds)) {
            bySearchCriteria = bySearchCriteria.and(event.category.id.in(catIds));
        }
        if (Objects.nonNull(start)) {
            bySearchCriteria = bySearchCriteria.and(event.eventDate.after(start));
        }
        if (Objects.nonNull(end)) {
            bySearchCriteria = bySearchCriteria.and(event.eventDate.before(end));
        }
        List<Event> foundItems = eventRepository.findAll(bySearchCriteria, PageRequest.of(from / size, size))
                .getContent();

        return foundItems.stream()
                .map(x -> addStatistics(x, start, end))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Event> getPublishedEvents(String text, List<Long> catIds, Boolean paid,
                                          LocalDateTime start, LocalDateTime end, Boolean onlyAvailable,
                                          SortType sortType, int from, int size) {

        if (Objects.nonNull(start) && Objects.nonNull(end) && start.isAfter(end)) {
            throw new IllegalParametersException("Incorrect date parameters");
        }

        QEvent event = QEvent.event;
        BooleanExpression bySearchCriteria = event.state.eq(State.PUBLISHED);
        if (Objects.nonNull(text)) {
            bySearchCriteria = bySearchCriteria.and(event.annotation.contains(text)
                    .or(event.description.contains(text)));
        }
        if (Objects.nonNull(catIds)) {
            bySearchCriteria = bySearchCriteria.and(event.category.id.in(catIds));
        }
        if (Objects.nonNull(paid)) {
            bySearchCriteria = bySearchCriteria.and(event.paid.eq(paid));
        }
        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            List<Long> availableEventIds = eventRepository.findAvailableEventIds();
            bySearchCriteria = bySearchCriteria.and(event.id.in(availableEventIds));
        }
        if (Objects.nonNull(start)) {
            bySearchCriteria = bySearchCriteria.and(QEvent.event.eventDate.after(start));
        }
        if (Objects.nonNull(end)) {
            bySearchCriteria = bySearchCriteria.and(QEvent.event.eventDate.before(end));
        }

        Sort sort = Sort.unsorted();
        if (Objects.nonNull(sortType) && sortType.equals(SortType.EVENT_DATE)) {
            sort = Sort.by(Sort.Direction.ASC, "eventDate");
        }
        PageRequest pr = PageRequest.of(from / size, size, sort);
        List<Event> result = eventRepository.findAll(bySearchCriteria, pr)
                .getContent();

        if (Objects.nonNull(sortType) && sortType.equals(SortType.VIEWS)) {
            return sortByViews(result, start, end);
        }

        return result;
    }

    @Override
    public void existEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ObjectNotFoundException(String.format("Event with id=%d not found", eventId));
        }
    }

    private Event addStatistics(Event event, LocalDateTime start, LocalDateTime end) {
        long confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED);
        event.setConfirmedRequests(confirmedRequests);

        List<StatsDto> stat = statisticsService.getStatistics(start, end, List.of("/events/" + event.getId()),
                true);

        if (stat != null) {
            event.setViews(stat.get(0).getHits());
        }

        return event;
    }

    private List<Event> sortByViews(List<Event> events, LocalDateTime start, LocalDateTime end) {
        List<String> uris = events.stream()
                .map(Event::getId)
                .map(String::valueOf)
                .map(x -> "/events/" + x)
                .collect(Collectors.toList());

        List<StatsDto> stat = statisticsService.getStatistics(start, end, uris, true);

        Map<Long, Long> views = new HashMap<>();


        if (stat != null) {
            stat.stream()
                    .filter(x -> x.getUri().contains("events"))
                    .filter(x -> x.getUri().split("/").length == 3)
                    .forEach(x -> {
                        Long id = Long.parseLong(x.getUri().split("/")[2]);
                        Long hits = x.getHits();
                        views.put(id, hits);
                    });

            for (Event event : events) {
                event.setViews(views.getOrDefault(event.getId(), 0L));
            }

            events.sort((x, y) -> y.getViews().intValue() - x.getViews().intValue());
        }

        return events;
    }

}
