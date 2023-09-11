package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VoteDTO {
    private String subject;

    private List<OptionDTO> options;

    @Builder
    public VoteDTO(String subject, List<OptionDTO> options) {
        this.subject = subject;
        this.options = options;
    }
}
