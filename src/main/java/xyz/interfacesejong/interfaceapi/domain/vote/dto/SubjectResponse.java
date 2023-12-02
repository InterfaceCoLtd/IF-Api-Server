package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.VoteSubject;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SubjectResponse {
    private Long subjectId;

    private String subject;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private int total;

    @Builder
    public SubjectResponse(Long subjectId, String subject, LocalDateTime startDate, LocalDateTime endDate, int total) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }

    public SubjectResponse(VoteSubject voteSubject){
        this.subject = voteSubject.getSubject();
        this.subjectId = voteSubject.getId();
        this.endDate = voteSubject.getEndDateTime();
        this.startDate = voteSubject.getStartDateTime();
        this.total = voteSubject.getTotal();
    }
}
