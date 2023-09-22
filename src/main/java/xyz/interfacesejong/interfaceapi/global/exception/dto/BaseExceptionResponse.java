package xyz.interfacesejong.interfaceapi.global.exception.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseExceptionResponse {
    private String exception;
}
