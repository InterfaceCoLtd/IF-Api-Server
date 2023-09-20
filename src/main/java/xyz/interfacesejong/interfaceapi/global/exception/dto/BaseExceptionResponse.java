package xyz.interfacesejong.interfaceapi.global.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseExceptionResponse {
    private String exception;

    public BaseExceptionResponse(String exception) {
        this.exception = exception;
    }
}
