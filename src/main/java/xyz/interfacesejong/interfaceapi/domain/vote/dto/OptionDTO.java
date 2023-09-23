package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OptionDTO {
    private Long optionId;

    private String option;

    private Integer count;


    @Builder
    public OptionDTO(String option, Long optionId, Integer count) {
        this.option = option;
        this.count = count;
        this.optionId = optionId;
    }
}
