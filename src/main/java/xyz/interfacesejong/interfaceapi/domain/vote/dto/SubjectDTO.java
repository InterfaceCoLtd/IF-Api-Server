package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubjectDTO {
    private Long subjectId;

    private String subject;

    private LocalDateTime startDate;

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
