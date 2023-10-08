package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class UserSignRequest {
    private String email;

    private String password;

    private UUID deviceId;


    public byte[] uuidToBinary(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }
}
