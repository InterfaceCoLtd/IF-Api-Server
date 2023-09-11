package xyz.interfacesejong.interfaceapi.vote.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.user.domain.User;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteSubject;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteVoter;


public interface VoteVoterRepository extends JpaRepository<VoteVoter, Long> {
    boolean existsByVoteSubjectAndUser(VoteSubject subject, User user);
}
