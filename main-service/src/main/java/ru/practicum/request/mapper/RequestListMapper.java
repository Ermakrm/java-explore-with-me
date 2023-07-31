package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = RequestMapper.class)
public interface RequestListMapper {
    List<RequestDto> toRequestDtoList(List<Request> requests);
}
