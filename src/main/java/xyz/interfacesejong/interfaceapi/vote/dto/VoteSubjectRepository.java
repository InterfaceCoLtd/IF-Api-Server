package xyz.interfacesejong.interfaceapi.vote.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteSubject;

import javax.persistence.Id;
import java.util.Optional;

public interface VoteSubjectRepository extends JpaRepository<VoteSubject, Id> {
    Optional<VoteSubject> findById(Long id);
}
