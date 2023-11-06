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
import xyz.interfacesejong.interfaceapi.global.util.BaseResponse;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
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
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());

        LOGGER.info("[findAllUsers] 모든 유저 조회");
        return users;
    }

    @Transactional
    public User saveUser(UserSignRequest signUpRequest){
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new EntityExistsException("ALREADY EXISTS USER");
        }

        User user = userRepository.save(User.builder()
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .authLevel(AuthLevelType.MEMBER_VERIFIED).build());
        //TODO 학생권한 부여는 임시 -> NEW ACCOUNT로 변경해야함

        LOGGER.info("[saveUser] 신규 유저 등록");
        return user;
    }

    @Transactional
    public BaseResponse hasEmail(String email){
        if (userRepository.existsByEmail(email)){
            return new BaseResponse("DUPLICATION");
        }

        LOGGER.info("[hasEmail] 이메일 중복 검사");
        return new BaseResponse("NON DUPLICATION");
    }

    @Transactional
    public User reRegisterPassword(Long id, UserNewPasswordRequest newPasswordRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[reRegisterPassword] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        user.reRegisterPassword(bCryptPasswordEncoder.encode(newPasswordRequest.getNewPassword()));
        user = userRepository.save(user);

        LOGGER.info("[reRegisterPassword] {} 신규 비밀번호 등록", id);
        return user;
    }
    @Transactional
    public User updateGeneration(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateGeneration] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getGeneration() == null){
            LOGGER.info("[updateGeneration] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else{
            user.changeGeneration(infoUpdateRequest.getGeneration());
        }

        user.changeAuthLevel(AuthLevelType.MEMBER_VERIFIED);
        user = userRepository.save(user);

        LOGGER.info("[updateGeneration] 기수 업데이트 성공");
        return user;
    }
    @Transactional
    public User updatePhoneNumber(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updatePhoneNumber] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getPhoneNumber() == null){
            LOGGER.info("[updatePhoneNumber] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changePhoneNumber(infoUpdateRequest.getPhoneNumber());
        }

        user = userRepository.save(user);
        LOGGER.info("[updatePhoneNumber] 전화번호 업데이트 성공");
        return user;
    }
    @Transactional
    public User updateGithubId(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateGithubId] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getGithubId() == null){
            LOGGER.info("[updateGithubId] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeGithubId(infoUpdateRequest.getGithubId());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateGithubId] github 계정 업데이트 성공");
        return user;
    }
    @Transactional
    public User updateDiscordId(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateDiscordId] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getDiscordId() == null){
            LOGGER.info("[updateDiscordId] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeDiscordId(infoUpdateRequest.getDiscordId());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateDiscordId] discord 계정 업데이트 성공");
        return user;
    }
    @Transactional
    public User updateFcmToken(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateFcmToken] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getFcmToken() == null){
            LOGGER.info("[updateFcmToken] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeFcmToken(infoUpdateRequest.getFcmToken());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateFcmToken] FcmToken 업데이트 성공");
        return user;
    }

    @Transactional
    public User updateDeviceId(Long id, UserInfoUpdateRequest infoUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateDeviceId] 등록 되지 않은 유저");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        if (infoUpdateRequest.getDeviceId() == null){
            LOGGER.info("[updateDeviceId] null 인자 입력");
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeDeviceId(infoUpdateRequest.getDeviceId());
        }

        user = userRepository.save(user);
        LOGGER.info("[updateDeviceId] device Id 갱신 성공");
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
