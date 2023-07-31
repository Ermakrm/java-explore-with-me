package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = CompilationMapper.class, componentModel = SPRING)
public interface CompilationListMapper {
    List<CompilationDto> toCompilationListDto(List<Compilation> compilations);
}
