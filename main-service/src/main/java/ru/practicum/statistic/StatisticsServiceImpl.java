package ru.practicum.statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticsClient;
import ru.practicum.dto.StatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsServiceImpl implements StatisticsService {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static LocalDateTime DEFAULT_START_DATE = LocalDateTime.parse("1990-01-01 00:00:00",
            DATE_FORMATTER);
    static LocalDateTime DEFAULT_END_DATE = LocalDateTime.parse("2099-01-01 00:00:00",
            DATE_FORMATTER);
    static String APP_NAME = "ewm-main-service";
    StatisticsClient statisticsClient;

    @Override
    public void saveStatistics(HttpServletRequest request) throws JsonProcessingException {
        statisticsClient.saveStatistics(APP_NAME, request.getRequestURI(), request.getRemoteAddr());
    }

    @Override
    public List<StatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startString;
        String endString;

        startString = Objects.requireNonNullElse(start, DEFAULT_START_DATE).format(DATE_FORMATTER);

        endString = Objects.requireNonNullElse(end, DEFAULT_END_DATE).format(DATE_FORMATTER);

        List<StatsDto> result = statisticsClient.getStatistics(startString, endString, uris, unique).getBody();

        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return null;
        }
    }
}
