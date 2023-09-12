package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vote_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteOption extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    Long id;

    String option;

    Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private VoteSubject voteSubject;

    @OneToMany(mappedBy = "voteOption", cascade = CascadeType.ALL)
    private List<VoteVoter> voteVoters;

    @Builder
    public VoteOption(Long id, String option, Integer count, VoteSubject voteSubject, List<VoteVoter> voteVoters) {
        this.id = id;
        this.option = option;
        this.count = count;
        this.voteSubject = voteSubject;
        this.voteVoters = voteVoters;
    }

    public void addCount(){
        ++count;
    }

}
