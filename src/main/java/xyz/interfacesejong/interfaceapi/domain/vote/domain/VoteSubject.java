package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vote_subject")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteSubject extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    private String subject;

    @OneToMany(mappedBy = "voteSubject", cascade = CascadeType.ALL)
    private List<VoteOption> voteOptions;

    @OneToMany(mappedBy = "voteSubject", cascade = CascadeType.ALL)
    private List<VoteVoter> voteVoters;

    @Builder
    public VoteSubject(Long id, String subject, List<VoteOption> voteOptions, List<VoteVoter> voteVoters) {
        this.id = id;
        this.subject = subject;
        this.voteOptions = voteOptions;
        this.voteVoters = voteVoters;
    }
}
