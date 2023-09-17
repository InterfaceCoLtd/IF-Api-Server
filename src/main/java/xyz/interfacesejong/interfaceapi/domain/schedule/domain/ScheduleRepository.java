package xyz.interfacesejong.interfaceapi.domain.schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.interfacesejong.interfaceapi.domain.schedule.dto.ScheduleDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.schedule.dto" +
            ".ScheduleDTO(s.id, s.title, s.description, s.startDate, s.endDate, s.allDay, s.type)" +
            " FROM Schedule s WHERE :dateTime >= s.startDate AND :dateTime <= s.endDate " +
            " ORDER BY s.startDate, s.endDate" )
    List<ScheduleDTO> findByDateTimeBetween(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.schedule.dto" +
            ".ScheduleDTO(s.id, s.title, s.description, s.startDate, s.endDate, s.allDay, s.type)" +
            "FROM Schedule s WHERE " +
            "(s.startDate <= :startOfMonth AND s.endDate >= :endOfMonth) OR " +
            "(s.startDate >= :startOfMonth AND s.endDate <= :endOfMonth) OR " +
            "(s.startDate <= :startOfMonth AND s.endDate >= :startOfMonth) OR " +
            "(s.startDate <= :endOfMonth AND s.endDate >= :endOfMonth)" +
            " ORDER BY s.startDate, s.endDate")
    List<ScheduleDTO> findByMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                  @Param("endOfMonth") LocalDateTime endOfMonth);

}
