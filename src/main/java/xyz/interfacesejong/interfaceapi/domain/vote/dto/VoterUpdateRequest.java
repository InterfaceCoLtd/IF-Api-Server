package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class VoterUpdateRequest {
    @NotNull
    private Long voterId;

    @NotNull
    private Long optionId;

    @Builder
    public VoterUpdateRequest(Long voterId, Long optionId) {
        this.voterId = voterId;
        this.optionId = optionId;
    }
}
