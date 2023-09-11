package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OptionDTO {
    private String option;

    @Builder
    public OptionDTO(String option) {
        this.option = option;
    }
}
