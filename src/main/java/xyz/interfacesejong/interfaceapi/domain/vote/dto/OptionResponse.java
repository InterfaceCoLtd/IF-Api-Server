package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OptionResponse {
    private String subject;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDateTime;

    private List<OptionDTO> options;

    @Builder
    public OptionResponse(String subject, List<OptionDTO> options, LocalDateTime endDateTime) {
        this.subject = subject;
        this.endDateTime = endDateTime;
        this.options = options;
    }
}
