package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.Getter;

@Getter
public class TitleDto {
    private String title;

    public TitleDto(String title) {
        this.title = title;
    }

}
