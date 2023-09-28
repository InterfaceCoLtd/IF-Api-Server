package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SejongStudentAuthRequest {
    private String sejongPortalId;
    private String sejongPortalPassword;
}
