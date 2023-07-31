package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.HitRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HitServiceImpl implements HitService {
    HitRepository hitRepository;
    HitMapper hitMapper;

    @Override
    public void save(HitRequestDto hitRequestDto) {
        hitRepository.save(hitMapper.toHit(hitRequestDto));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                   Boolean unique) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong timestamp");
        }

        List<StatsDto> result;
        List<String> clearedUris = uris.stream()
                .map(x -> x.replace("[", "").replace("]", ""))
                .collect(Collectors.toList());

        if (!unique) {
            if (uris.isEmpty()) {
                result = hitRepository.countAllBetween(start, end).stream()
                        .map(hitMapper::toHitResponseDto).collect(Collectors.toList());
            } else {
                result = hitRepository.countAllByUrisBetween(start, end, clearedUris).stream()
                        .map(hitMapper::toHitResponseDto).collect(Collectors.toList());
            }
        } else {
            if (uris.isEmpty()) {
                result = toHitResponseDto(hitRepository.countUniqueBetween(start, end));
            } else {
                result = toHitResponseDto(hitRepository.countUniqueByUrisBetween(start, end, clearedUris));
            }
        }

        return result;
    }

    private List<StatsDto> toHitResponseDto(List<Object[]> result) {
        List<StatsDto> statsDtoList = new ArrayList<>();

        result.forEach(x -> {
            StatsDto statsDto = new StatsDto((String) x[0], (String) x[1],
                    ((BigInteger) x[2]).longValue());
            statsDtoList.add(statsDto);
        });

        return statsDtoList;
    }

}
