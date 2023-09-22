package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.VoterDTO;

import java.util.List;


public interface VoteVoterRepository extends JpaRepository<VoteVoter, Long> {
    boolean existsByVoteSubjectAndUser(VoteSubject subject, User user);

    @Query(" SELECT new xyz.interfacesejong.interfaceapi.domain.vote.dto.VoterDTO(vv.id, vv.voteSubject.id, vv.voteOption.id, vv.user.id)" +
            " FROM VoteVoter vv" +
            " WHERE vv.voteSubject.id = :subjectId AND vv.user.id = :userId")
    List<VoterDTO> findByVoteSubjectIdAndUserId(Long subjectId, Long userId);

}
