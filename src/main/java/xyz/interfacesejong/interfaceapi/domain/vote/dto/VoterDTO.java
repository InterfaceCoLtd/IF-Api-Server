package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VoterDTO {
    private Long voterId;

    private Long subjectId;

    private Long optionId;

    private Long userId;

    @Builder
    public VoterDTO(Long voterId, Long subjectId, Long optionId, Long userId) {
        this.voterId = voterId;
        this.subjectId = subjectId;
        this.optionId = optionId;
        this.userId = userId;
    }
}
