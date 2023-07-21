package ru.practicum.ewm.stats.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.ewm.stats.model.Hit;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    Hit toHit(HitRequestDto hitRequestDto);

    HitRequestDto toHitRequestDto(Hit hit);
}
