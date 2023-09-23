package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SubjectRequest {
    private String subject;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private List<OptionRequest> options;

    @Builder
    public SubjectRequest(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime, List<OptionRequest> options) {
        this.subject = subject;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.options = options;
    }
}
