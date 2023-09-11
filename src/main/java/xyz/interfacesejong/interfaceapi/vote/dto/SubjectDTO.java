package xyz.interfacesejong.interfaceapi.vote.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubjectDTO {
    String subject;

    @Builder
    public SubjectDTO(String subject) {
        this.subject = subject;
    }
}
