package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SubjectRequest {
    private String subject;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
