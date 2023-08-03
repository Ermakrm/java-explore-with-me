package ru.practicum.location.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class LocationServiceImpl implements LocationService {
    LocationRepository locationRepository;

    @Override
    public void findOrSaveLocation(Location location) {
        if (!locationRepository.existsByLatAndLon(location.getLat(), location.getLon())) {
            locationRepository.save(location);
        }
    }
}
