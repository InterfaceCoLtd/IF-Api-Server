package xyz.interfacesejong.interfaceapi.domain.vote.dto;

import lombok.Getter;
import lombok.ToString;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.VoteOption;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.VoteSubject;

import java.util.List;

@Getter
@ToString
public class CreateResponse {
    VoteSubject subject;

    List<VoteOption> options;

    public CreateResponse(VoteSubject subject, List<VoteOption> options) {
        this.subject = subject;
        this.options = options;
    }
}
