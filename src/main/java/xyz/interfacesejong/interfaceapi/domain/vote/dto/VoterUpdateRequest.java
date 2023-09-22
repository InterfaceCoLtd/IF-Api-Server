package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
