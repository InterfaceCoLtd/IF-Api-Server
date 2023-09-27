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
    @GetMapping("exists")
    @Operation(summary = "이메일 중복 검사", description = "해당 이메일이 db에 존재하는 계정인지 확인합니다.")
    public ResponseEntity<?> checkEmailDuplication(@RequestParam String email){
        return null;
    }

    //이름
    @Timer
    @PutMapping("user/{id}/username")
    @Operation(summary = "이름 변경", description = "해당 id 유저의 이름을 변경한다.")
    public ResponseEntity<?> updateUsername(@PathVariable Long id){
        return null;
    }

    //비밀번호
    @Timer
    @PutMapping("user/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "해당 id 유저의 비밀번호를 변경한다.")
    public ResponseEntity<?> updatePassword(@PathVariable Long id){
        return null;
    }

    //전화번호 char
    @Timer
    @PutMapping("user/{id}/phone-number")
    @Operation(summary = "전화번호 변경", description = "해당 id 유저의 전화번호를 변경한다.")
    public ResponseEntity<?> updatePhoneNumber(@PathVariable Long id){
        return null;
    }

    //github
    @Timer
    @PutMapping("user/{id}/github-account")
    @Operation(summary = "gitbub아이디 변경", description = "해당 id 유저의 github 아이디를 변경한다.")
    public ResponseEntity<?> updateGithubAccount(@PathVariable Long id){
        return null;
    }

    //discord
    @Timer
    @PutMapping("user/{id}/discord-account")
    @Operation(summary = "dicord아이디 변경", description = "해당 id 유저의 discord아이디를 변경한다.")
    public ResponseEntity<?> updateDiscordAccount(@PathVariable Long id) {
        return null;
    }
}
