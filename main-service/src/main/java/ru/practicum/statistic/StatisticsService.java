package ru.practicum.statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void saveStatistics(HttpServletRequest request) throws JsonProcessingException;

    List<StatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
