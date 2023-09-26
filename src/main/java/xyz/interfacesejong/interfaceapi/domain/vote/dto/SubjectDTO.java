package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SubjectDTO {
    private Long subjectId;

    private String subject;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private int total;

    @Builder
    public SubjectDTO(Long subjectId, String subject, LocalDateTime startDate, LocalDateTime endDate, int total) {
        this.subjectId = subjectId;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }
}
