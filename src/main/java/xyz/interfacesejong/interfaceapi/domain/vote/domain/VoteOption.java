package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vote_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteOption extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    private String option;

    @Column(insertable = true)
    private int count = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private VoteSubject voteSubject;

    @OneToMany(mappedBy = "voteOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteVoter> voteVoters = new ArrayList<>();;

    @Builder
    public VoteOption(Long id, String option, int count, VoteSubject voteSubject, List<VoteVoter> voteVoters) {
        this.id = id;
        this.option = option;
        this.count = count;
        this.voteSubject = voteSubject;
        this.voteVoters = voteVoters;
    }

    public void increaseCount(){
        ++(this.count);
    }
    
    public void decreaseCount(){
        --(this.count);
    }

}
