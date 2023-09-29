package xyz.interfacesejong.interfaceapi.global.util;

import lombok.Data;

@Data
public class BaseResponse {
    private String response;

    public BaseResponse() {
    }

    public BaseResponse(String response) {
        this.response = response;
    }
}
