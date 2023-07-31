package ru.practicum.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EventMapper.class,
        componentModel = SPRING)
public interface CompilationMapper {
    CompilationDto toCompilationDto(Compilation compilation);

    default Event mapIdToEvent(Long id) {
        Event event = new Event();
        event.setId(id);

        return event;
    }

    @Mapping(source = "pinned", target = "pinned", defaultValue = "false")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    void updateCompilation(UpdateCompilationRequest updateCompilationRequest, @MappingTarget Compilation compilation);

}
