package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OptionResponse {
    private String subject;

    private LocalDateTime endDateTime;

    private List<OptionDTO> options;

    @Builder
    public OptionResponse(String subject, List<OptionDTO> options, LocalDateTime endDateTime) {
        this.subject = subject;
        this.endDateTime = endDateTime;
        this.options = options;
    }
}
