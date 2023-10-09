package xyz.interfacesejong.interfaceapi.domain.Schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.Schedule.dto" +
            ".ScheduleResponse(s.id, s.title, s.description, s.startDate, s.endDate, s.allDay, s.type, s.boardId)" +
            " FROM Schedule s WHERE :dateTime >= s.startDate AND :dateTime <= s.endDate " +
            " ORDER BY s.startDate, s.endDate" )
    List<ScheduleResponse> findByDateTimeBetween(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.Schedule.dto" +
            ".ScheduleResponse(s.id, s.title, s.description, s.startDate, s.endDate, s.allDay, s.type, s.boardId)" +
            "FROM Schedule s WHERE " +
            "(s.startDate <= :startOfMonth AND s.endDate >= :endOfMonth) OR " +
            "(s.startDate >= :startOfMonth AND s.endDate <= :endOfMonth) OR " +
            "(s.startDate <= :startOfMonth AND s.endDate >= :startOfMonth) OR " +
            "(s.startDate <= :endOfMonth AND s.endDate >= :endOfMonth)" +
            " ORDER BY s.startDate, s.endDate")
    List<ScheduleResponse> findByMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                       @Param("endOfMonth") LocalDateTime endOfMonth);

}
