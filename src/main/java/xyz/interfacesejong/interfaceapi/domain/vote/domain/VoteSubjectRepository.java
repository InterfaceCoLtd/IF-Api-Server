package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteSubjectRepository extends JpaRepository<VoteSubject, Long> {
    Optional<VoteSubject> findById(Long id);

    List<VoteSubject> findAllByOrderByStartDateTimeDesc();

    @Query("SELECT s " +
            "FROM VoteSubject s " +
            "WHERE s.startDateTime <= :now AND :now <= s.endDateTime " +
            "ORDER BY s.startDateTime, s.id")
    List<VoteSubject> findAllByOngoingSubjects(LocalDateTime now);

    @Query("SELECT s " +
            "FROM VoteSubject s " +
            "WHERE s.endDateTime <= :now" +
            " ORDER BY s.startDateTime, s.id")
    List<VoteSubject> findAllByCompletedSubjects(LocalDateTime now);

    @Query("SELECT s " +
            "FROM VoteSubject s " +
            "WHERE s.startDateTime >= :now " +
            "ORDER BY s.startDateTime, s.id")
    List<VoteSubject> findAllByUpcomingSubjects(LocalDateTime now);

}
