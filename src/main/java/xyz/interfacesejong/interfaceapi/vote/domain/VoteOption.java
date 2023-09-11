package xyz.interfacesejong.interfaceapi.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vote_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    Long id;

    String option;

    Integer count;

    @ManyToOne
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
