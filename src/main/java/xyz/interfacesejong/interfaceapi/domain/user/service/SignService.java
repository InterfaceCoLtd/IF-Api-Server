package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserSignRequest;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserSignResponse;
import xyz.interfacesejong.interfaceapi.global.email.EmailSender;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;
import xyz.interfacesejong.interfaceapi.global.jwt.TokenProvider;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SignService {
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);


    public AuthEmailResponse sendVerifyMail(UserSignResponse response){
        String verifyToken = tokenProvider.generateToken(response);
        Map<String, Object> variables = new HashMap<>();
        variables.put("authCode", verifyToken);
        try {
            return emailSender.sendMessage(response.getEmail(), variables, "verifyEmailTemplate");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void verifyUser(String token){
        Long userId;
        if (tokenProvider.isValidatedToken(token)){
            userId = tokenProvider.getUserId(token);
        }else {
            throw new IllegalArgumentException("INVALID TOKEN");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()->{
                    LOGGER.info("[verifyUser] 등록되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS EXCEPTION");
                });

        user.changeAuthLevel(AuthLevelType.MAIL_VERIFIED);
        userRepository.save(user);
        LOGGER.info("[verifyUser] userId {} mail 인증 완료", userId);
    }

    public UserSignResponse signIn(UserSignRequest signRequest){
        User user = userRepository.findByEmail(signRequest.getEmail())
                .orElseThrow(() -> {
                    LOGGER.info("[signIn] 이메일 오류");
                    return new IllegalArgumentException("INVALID ARGUMENT");
                });

        if (!bCryptPasswordEncoder.matches(signRequest.getPassword(), user.getPassword())){
            LOGGER.info("[signIn] 비밀번호 오류");
            throw new IllegalArgumentException("INVALID ARGUMENT");
        }

        if (user.getAuthLevel() == AuthLevelType.NEW_ACCOUNT){
            throw new AuthenticationServiceException("UNAUTHENTICATED MAIL ACCOUNT");
        }

        if (signRequest.getDeviceId() != null){
            userRepository.updateDeviceIdToNull(signRequest.getDeviceId());
            userRepository.flush();
            user.changeDeviceId(signRequest.getDeviceId());
            userRepository.updateDeviceIdByUserId(user.getId(), signRequest.getDeviceId());
        }

        LOGGER.info("[signIn] {} 로그인 성공", signRequest.getEmail());
        return new UserSignResponse(user);
    }

    public UserSignResponse simpleSignIn(UserSignRequest signRequest){

        User user = userRepository.findByDeviceId(signRequest.getDeviceId())
                .orElseThrow(() -> {
                    LOGGER.info("[simpleSignIn] 잘못된 기기 정보");
                    return new IllegalArgumentException("NOT REGISTER DEVICE ID");
                });

        LOGGER.info("[simpleSignIn] 서브 로그인 정보 통과");
        return new UserSignResponse(user);
    }
}
