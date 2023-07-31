package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.Status;
import ru.practicum.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    Request findByIdAndRequesterId(Long requestId, Long userId);

    List<Request> findAllByEventIdAndStatus(Long eventId, Status status);

    Integer countAllByEventId(Long eventId);

    Integer countAllByEventIdAndStatus(Long eventId, Status status);

    Boolean existsAllByStatusAndIdIn(Status status, List<Long> ids);

    @Modifying
    @Query(value = "UPDATE Request r SET r.status = :status WHERE r.id IN :ids")
    void updateRequestStatus(@Param("status") Status status, @Param("ids") List<Long> ids);
}
