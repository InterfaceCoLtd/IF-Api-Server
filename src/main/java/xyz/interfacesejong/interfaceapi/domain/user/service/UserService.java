package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.user.dto.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SejongStudentAuth sejongStudentAuth;

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public List<UserInfoResponse> findAllUsers(){
        List<UserInfoResponse> users = userRepository.findAll().stream()
                .map(user -> UserInfoResponse.builder()
                        .email(user.getEmail())
                        .id(user.getId())
                        .build()).collect(Collectors.toList());

        LOGGER.info("[findAllUsers] 모든 유저 조회");
        return users;
    }

    @Transactional
    public User saveUser(UserSignUpRequest signUpRequest){
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new EntityExistsException("ALREADY EXISTS USER");
        }

        User user = userRepository.save(User.builder()
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .authLevel(AuthLevelType.NEW_ACCOUNT).build());

        LOGGER.info("[saveUser] 신규 유저 등록");
        return user;
    }

    @Transactional
    public Map<String, Boolean> hasEmail(String email){
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("duplication",userRepository.existsByEmail(email));
        LOGGER.info("[hasEmail] 이메일 중복 검사");
        return result;
    }

    @Transactional
    public User reRegisterPassword(Long id, UserNewPasswordRequest newPasswordRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[GenerateNewPassword] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        user.reRegisterPassword(bCryptPasswordEncoder.encode(newPasswordRequest.getNewPassword()));
        user = userRepository.save(user);

        LOGGER.info("[reRegisterPassword] {} 신규 비밀번호 등록", id);
        return user;
    }
    @Transactional
    public User updateGeneration(Long id, UserInfoUpdateRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateGeneration] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoRequest.getGeneration() == null){
            LOGGER.info("[updateGeneration] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else{
            user.changeGeneration(infoRequest.getGeneration());
        }

        user.changeAuthLevel(AuthLevelType.MEMBER_VERIFIED);
        user = userRepository.save(user);

        LOGGER.info("[updateGeneration] 기수 업데이트 성공");
        return user;
    }
    @Transactional
    public User updatePhoneNumber(Long id, UserInfoUpdateRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updatePhoneNumber] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoRequest.getPhoneNumber() == null){
            LOGGER.info("[updatePhoneNumber] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changePhoneNumber(infoRequest.getPhoneNumber());
        }

        user = userRepository.save(user);
        LOGGER.info("[updatePhoneNumber] 전화번호 업데이트 성공");
        return user;
    }
    @Transactional
    public User updateGithubId(Long id, UserInfoUpdateRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateGithubId] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoRequest.getGithubId() == null){
            LOGGER.info("[updateGithubId] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeGithubId(infoRequest.getGithubId());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateGithubId] github 계정 업데이트 성공");
        return user;
    }
    @Transactional
    public User updateDiscordId(Long id, UserInfoUpdateRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateDiscordId] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });
        
        if (infoRequest.getDiscordId() == null){
            LOGGER.info("[updateDiscordId] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeDiscordId(infoRequest.getDiscordId());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateDiscordId] discord 계정 업데이트 성공");
        return user;
    }

    @Transactional
    public User updateSejongStudentAuth(Long id, SejongStudentAuthRequest sejongStudentAuthRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateSejongStudentAuth] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        try {
            user.updateSejongAuthInfo(
                    sejongStudentAuth.getUserAuthInfos(sejongStudentAuthRequest.getSejongPortalId(), sejongStudentAuthRequest.getSejongPortalPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.changeAuthLevel(AuthLevelType.STUDENT_VERIFIED);
        user = userRepository.save(user);

        LOGGER.info("[updateSejongStudentAuth] 세종대 학생 정보 인증 완료");
        return user;
    }


}
