package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserInfoRequest;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserInfoResponse;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserSignUpRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword())).build());

        LOGGER.info("[saveUser] 신규 유저 등록");
        return user;
    }

    /*
    private String password;

    private Integer generation;

    */
    @Transactional
    public void updateGeneration(Long id, UserInfoRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST USER"));

        if (infoRequest.getGeneration() == null){
            throw new IllegalArgumentException("MISSING FIELD");
        }else{
            user.changeGeneration(infoRequest.getGeneration());
        }

        userRepository.save(user);
    }
    @Transactional
    public void updatePhoneNumber(Long id, UserInfoRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST USER"));

        if (infoRequest.getPhoneNumber() == null){
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changePhoneNumber(infoRequest.getPhoneNumber());
        }


        userRepository.save(user);
    }
    @Transactional
    public void updateGithubId(Long id, UserInfoRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST USER"));

        if (infoRequest.getGithubId() == null){
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeGithubId(infoRequest.getGithubId());
        }

        userRepository.save(user);
    }
    @Transactional
    public void updateDiscordId(Long id, UserInfoRequest infoRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST USER"));
        if (infoRequest.getDiscordId() == null){
            throw new IllegalArgumentException("MISSING FIELD");
        }else {
            user.changeDiscordId(infoRequest.getDiscordId());
        }

        userRepository.save(user);
    }


}
