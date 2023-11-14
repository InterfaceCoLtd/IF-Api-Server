package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSliceResponse {
    private List<BoardResponse> boardResponses = new ArrayList<>();
    private boolean first;
    private boolean last;

    @Builder
    public BoardSliceResponse(List<BoardResponse> boardResponses, boolean first, boolean last) {
        this.boardResponses = boardResponses;
        this.first = first;
        this.last = last;
    }
}
