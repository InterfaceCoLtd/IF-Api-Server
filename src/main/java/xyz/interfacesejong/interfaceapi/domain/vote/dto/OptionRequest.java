package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OptionRequest {
    private String Option;

    public void setOption(String option) {
        Option = option;
    }
}
