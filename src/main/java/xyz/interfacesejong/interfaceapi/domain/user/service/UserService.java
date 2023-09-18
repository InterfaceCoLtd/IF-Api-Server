package xyz.interfacesejong.interfaceapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserDTO;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserInfoResponse;

import javax.persistence.EntityExistsException;
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
    public User save(UserDTO userDTO){
        if (userRepository.existsByEmail(userDTO.getEmail())){
            throw new EntityExistsException("ALREADY EXISTS USER");
        }

        User user = userRepository.save(User.builder()
                .email(userDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword())).build());

        LOGGER.info("[save] 신규 유저 등록");
        return user;
    }


}
