package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OptionResponse {
    private String subject;

    private List<OptionDTO> options;

    private Integer total;

    @Builder
    public OptionResponse(String subject, List<OptionDTO> options, Integer total) {
        this.subject = subject;
        this.options = options;
        this.total = total;
    }
}
