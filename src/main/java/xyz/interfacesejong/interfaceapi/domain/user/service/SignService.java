package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserSignRequest;
import xyz.interfacesejong.interfaceapi.global.email.EmailSender;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;
import xyz.interfacesejong.interfaceapi.global.jwt.TokenProvider;
import xyz.interfacesejong.interfaceapi.global.util.BaseResponse;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignService {
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);


    /* TODO 이메일 기능 수정
    public AuthEmailResponse sendVerifyMail(Long id, String email){
        String verifyToken = tokenProvider.generateToken(id, email);
        Map<String, Object> variables = new HashMap<>();
        variables.put("authCode", verifyToken);
        try {
            return emailSender.sendMessage(email, variables, "verifyEmailTemplate");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }*/

    public BaseResponse signIn(UserSignRequest signRequest){
        BaseResponse baseResponse = new BaseResponse();
        Optional<User> userOptional = userRepository.findByEmail(signRequest.getEmail());
        User user;
        if (userOptional.isEmpty()){
            LOGGER.info("[signIn] 이메일 오류");
            baseResponse.setResponse("INVALID ARGUMENT");
            return baseResponse;
        }else {
            user = userOptional.get();
        }

        if (!bCryptPasswordEncoder.matches(signRequest.getPassword(), user.getPassword())){
            LOGGER.info("[signIn] 비밀번호 오류");
            baseResponse.setResponse("INVALID ARGUMENT");
            return baseResponse;
        }

        if (user.getAuthLevel() == AuthLevelType.NEW_ACCOUNT){
            baseResponse.setResponse("UNAUTHENTICATED MAIL ACCOUNT");
            return baseResponse;
        }

        LOGGER.info("[signIn] {} 로그인 성공", signRequest.getEmail());
        baseResponse.setResponse("SIGN IN SUCCESS");
        return baseResponse;
    }

    public User simpleSignIn(UserSignRequest signRequest){
        User user = userRepository.findByEmail(signRequest.getEmail())
                .orElseThrow(() -> {
                    LOGGER.info("[simpleSignIn] 이메일 오류");
                    return new IllegalArgumentException("INVALID ARGUMENT");
                });

        if (!user.getDeviceId().equals(signRequest.getDeviceId())){
            LOGGER.info("[simpleSignIn] 기기 번호 오류");
            System.out.println(user.getDeviceId());
            throw new IllegalArgumentException("INVALID ARGUMENT");
        }

        LOGGER.info("[simpleSignIn] 서브 로그인 정보 통과");
        return user;
    }
}
