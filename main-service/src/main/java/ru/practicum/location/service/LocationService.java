package ru.practicum.location.service;

import ru.practicum.location.model.Location;

public interface LocationService {
    void findOrSaveLocation(Location location);
}
