package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.SubjectDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteSubjectRepository extends JpaRepository<VoteSubject, Long> {
    Optional<VoteSubject> findById(Long id);

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.vote.dto" +
            ".SubjectDTO(s.id, s.subject, s.startDateTime, s.endDateTime, s.total)" +
            " FROM VoteSubject s WHERE s.startDateTime <= :now AND :now <= s.endDateTime" +
            " ORDER BY s.startDateTime, s.id")
    List<SubjectDTO> findAllByOngoing(LocalDateTime now);

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.vote.dto" +
            ".SubjectDTO(s.id, s.subject, s.startDateTime, s.endDateTime, s.total)" +
            " FROM VoteSubject s WHERE s.endDateTime <= :now" +
            " ORDER BY s.startDateTime, s.id")
    List<SubjectDTO> findAllByCompleted(LocalDateTime now);

    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.vote.dto" +
            ".SubjectDTO(s.id, s.subject, s.startDateTime, s.endDateTime, s.total)" +
            " FROM VoteSubject s WHERE s.startDateTime >= :now" +
            " ORDER BY s.startDateTime, s.id")
    List<SubjectDTO> findAllByUpcoming(LocalDateTime now);
}
