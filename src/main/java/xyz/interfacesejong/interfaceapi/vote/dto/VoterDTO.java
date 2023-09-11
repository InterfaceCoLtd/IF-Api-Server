package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VoterDTO {
    private Long subjectId;

    private Long optionId;

    private Long userId;

    @Builder
    public VoterDTO(Long subjectId, Long optionId, Long userId) {
        this.subjectId = subjectId;
        this.optionId = optionId;
        this.userId = userId;
    }
}
