package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit toHit(HitRequestDto hitRequestDto);

    StatsDto toHitResponseDto(StatsDto statsDto);
}
