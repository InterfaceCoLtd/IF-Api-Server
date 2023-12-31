package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDateTime;

    @Column(insertable = true)
    private int total = 0;

    @OneToMany(mappedBy = "voteSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteOption> voteOptions = new ArrayList<>();;

    @OneToMany(mappedBy = "voteSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteVoter> voteVoters = new ArrayList<>();;

    @Builder
    public VoteSubject(Long id, String subject, LocalDateTime startDateTime, LocalDateTime endDateTime, int total, List<VoteOption> voteOptions, List<VoteVoter> voteVoters) {
        this.id = id;
        this.subject = subject;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.total = total;
        this.voteOptions = voteOptions;
        this.voteVoters = voteVoters;
    }

    public void addTotal(){
        ++(this.total);
    }

    public void removeTotal(){
        --(this.total);
    }

    @PrePersist
    public void dropMilliSecond(){
        this.startDateTime = this.startDateTime.withNano(0);
        this.endDateTime = this.endDateTime.withNano(0);
    }
}
