package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteSubjectRepository extends JpaRepository<VoteSubject, Long> {
    Optional<VoteSubject> findById(Long id);
}
