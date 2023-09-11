package xyz.interfacesejong.interfaceapi.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.user.domain.User;

import javax.persistence.*;

@Entity
@Table(name = "vote_voter")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteVoter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private VoteSubject voteSubject;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private VoteOption voteOption;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public VoteVoter(Long id, VoteSubject voteSubject, VoteOption voteOption, User user) {
        this.id = id;
        this.voteSubject = voteSubject;
        this.voteOption = voteOption;
        this.user = user;
    }
}
