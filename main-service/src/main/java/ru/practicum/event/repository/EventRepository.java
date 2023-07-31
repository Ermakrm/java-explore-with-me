package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, State state);

    @Query(value = "SELECT a.id FROM (" +
            " SELECT id, participant_limit, q.qty, (participant_limit = 0 OR participant_limit > q.qty OR q.qty ISNULL)" +
            " AS available" +
            " FROM events e" +
            " LEFT JOIN (SELECT event_id, COUNT(*) qty FROM request r " +
            " WHERE r.status = 'CONFIRMED'" +
            " GROUP BY r.event_id) AS q" +
            " ON q.event_id = e.id" +
            " ) AS a" +
            " WHERE a.available", nativeQuery = true)
    List<Long> findAvailableEventIds();
}
