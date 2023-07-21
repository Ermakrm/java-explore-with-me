package ru.practicum.ewm.stats.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.StatsDto;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsServiceImpl implements StatsService {

    StatsRepository repository;

    @Override
    public Hit save(Hit hit) {
        return repository.save(hit);
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong timestamp");
        }
        if (unique) {
            if (uris != null) {
                return repository.findUniqueHitsIpWithUris(uris, start, end);
            }
            return repository.findUniqueHits(start, end);
        } else {
            if (uris != null) {
                return repository.findAllHitsWithUris(uris, start, end);
            }
            return repository.findAllHits(start, end);
        }
    }
}
