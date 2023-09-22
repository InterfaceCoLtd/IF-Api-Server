package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;

@Entity
@Table(name = "vote_voter")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteVoter extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private VoteSubject voteSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private VoteOption voteOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public VoteVoter(Long id, VoteSubject voteSubject, VoteOption voteOption, User user) {
        this.id = id;
        this.voteSubject = voteSubject;
        this.voteOption = voteOption;
        this.user = user;
    }

    public void updateOption(VoteOption option){
        this.voteOption = option;
    }
}
