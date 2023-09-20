package xyz.interfacesejong.interfaceapi.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserDTO;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserInfoResponse;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;
import xyz.interfacesejong.interfaceapi.global.email.AuthEmail;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    private final AuthEmail authEmail;
    @Timer
    @PostMapping()
    @Operation(summary = "신규 유저 등록", description = "신규 유저를 생성합니다.")
    public ResponseEntity<User> createUser(UserDTO userDTO){
        return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);
    }

    @Timer
    @GetMapping()
    @Operation(summary = "전체 유저 조회", description = "모든 유저를 조회합니다.")
    public ResponseEntity<List<UserInfoResponse>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Timer
    @PostMapping("auth/email/{email}")
    public ResponseEntity<AuthEmailResponse> sendAuthMail(@PathVariable String email) throws MessagingException {
        AuthEmailResponse authEmailResponse = authEmail.sendMessage(email);
        return new ResponseEntity<>(authEmailResponse, HttpStatus.OK);
    }
}
