package ru.practicum.ewm.stats.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.StatsDto;
import ru.practicum.ewm.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {
    Hit save(Hit hit);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
