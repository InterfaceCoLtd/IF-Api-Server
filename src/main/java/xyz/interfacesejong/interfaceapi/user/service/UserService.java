package xyz.interfacesejong.interfaceapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.user.domain.User;
import xyz.interfacesejong.interfaceapi.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.user.dto.UserInfoResponse;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public List<UserInfoResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserInfoResponse.builder()
                        .email(user.getEmail())
                        .id(user.getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(String email, String password) throws Exception{
        Optional<User> isExistUser = userRepository.findByEmail(email);
        if (isExistUser.isPresent()){
            throw new EntityExistsException("이미 등록된 계정입니다.");
        }

        User user = User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .build();

        userRepository.save(user);
    }
}
