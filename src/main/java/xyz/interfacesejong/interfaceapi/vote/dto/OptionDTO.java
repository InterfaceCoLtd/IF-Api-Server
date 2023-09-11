package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OptionDTO {
    private String option;

    private Integer count;

    @Builder
    public OptionDTO(String option, Integer count) {
        this.option = option;
        this.count = count;
    }
}
