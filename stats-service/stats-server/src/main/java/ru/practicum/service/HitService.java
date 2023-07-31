package ru.practicum.service;

import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void save(HitRequestDto hitRequestDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
