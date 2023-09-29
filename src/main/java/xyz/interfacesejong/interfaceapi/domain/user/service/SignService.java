package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.global.email.EmailSender;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;
import xyz.interfacesejong.interfaceapi.global.jwt.TokenProvider;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignService {
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    public AuthEmailResponse sendVerifyMail(Long id, String email){
        String verifyToken = tokenProvider.generateToken(id, email);
        Map<String, Object> variables = new HashMap<>();
        variables.put("authCode", verifyToken);
        try {
            return emailSender.sendMessage(email, variables, "verifyEmailTemplate");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
