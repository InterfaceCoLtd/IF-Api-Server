package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.Getter;
import lombok.ToString;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.VoteSubject;

import java.util.List;

@Getter
@ToString
public class CreateResponse {
    VoteSubject subject;

    List<OptionDTO> options;

    public CreateResponse(VoteSubject subject, List<OptionDTO> options) {
        this.subject = subject;
        this.options = options;
    }
}
