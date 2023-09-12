package xyz.interfacesejong.interfaceapi.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.user.domain.User;


public interface VoteVoterRepository extends JpaRepository<VoteVoter, Long> {
    boolean existsByVoteSubjectAndUser(VoteSubject subject, User user);
}
