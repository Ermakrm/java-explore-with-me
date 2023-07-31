package ru.practicum.event.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, CategoryMapper.class}, componentModel = SPRING)
@Component
public interface EventMapper {
    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    @Mapping(source = "category", target = "category.id")
    @Mapping(source = "paid", target = "paid", defaultValue = "false")
    @Mapping(source = "requestModeration", target = "requestModeration", defaultValue = "true")
    @Mapping(source = "participantLimit", target = "participantLimit", defaultValue = "0")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "updateEventAdminRequest.category", target = "event.category.id")
    void updateEvent(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event event);

    @Mapping(source = "updateEventAdminRequest.category", target = "event.category.id")
    void updateEvent(UpdateEventUserRequest updateEventAdminRequest, @MappingTarget Event event);
}
