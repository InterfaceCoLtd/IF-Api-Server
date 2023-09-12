package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubjectDTO {
    private Long subjectId;

    private String subject;

    @Builder
    public SubjectDTO(String subject, Long subjectId) {
        this.subject = subject;
        this.subjectId = subjectId;
    }
}
