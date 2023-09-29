package xyz.interfacesejong.interfaceapi.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.dto.*;
import xyz.interfacesejong.interfaceapi.domain.user.service.SignService;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;
import xyz.interfacesejong.interfaceapi.global.util.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final SignService signService;

    @Timer
    @PostMapping()
    @Operation(summary = "신규 유저 등록", description = "신규 유저를 생성합니다.")
    public ResponseEntity<User> createUser(UserSignRequest signUpRequest){
        return new ResponseEntity<>(userService.saveUser(signUpRequest), HttpStatus.CREATED);
    }

    @Timer
    @GetMapping()
    @Operation(summary = "전체 유저 조회", description = "모든 유저를 조회합니다. \n\n 응답 형식 JSON, {\"duplication\"=\"true\" 수정 예정")
    public ResponseEntity<List<UserInfoResponse>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Timer
    @GetMapping("exists")
    @Operation(summary = "이메일 중복 검사", description = "해당 이메일이 db에 존재하는 계정인지 확인합니다.")
    public ResponseEntity<BaseResponse> checkEmailDuplication(@RequestParam String email){
        return new ResponseEntity<>(userService.hasEmail(email), HttpStatus.OK);
    }

    @Timer
    @PostMapping("auth/sign-in")
    @Operation(summary = "로그인 요청", description = "로그인 요청 기능")
    public ResponseEntity<BaseResponse> signIn(UserSignRequest signInRequest){
        return new ResponseEntity<>(signService.signIn(signInRequest), HttpStatus.OK);
    }


    //비밀번호
    @Timer
    @PutMapping("user/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "해당 id 유저의 비밀번호를 변경한다.")
    public ResponseEntity<User> generateNewPassword(@PathVariable Long id, @RequestBody UserNewPasswordRequest newPasswordRequest){
        return new ResponseEntity<>(userService.reRegisterPassword(id, newPasswordRequest), HttpStatus.OK);
    }

    //기수
    @Timer
    @PutMapping("user/{id}/generation")
    @Operation(summary = " 기수 변경", description = "해당 id 유저의 기수를 변경한다.")
    public ResponseEntity<User> updateGeneration(@PathVariable Long id, @RequestBody UserInfoUpdateRequest infoUpdateRequest){
        return new ResponseEntity<>(userService.updateGeneration(id, infoUpdateRequest), HttpStatus.OK);
    }

    //전화번호
    @Timer
    @PutMapping("user/{id}/phone-number")
    @Operation(summary = "전화번호 변경", description = "해당 id 유저의 전화번호를 변경한다.")
    public ResponseEntity<User> updatePhoneNumber(@PathVariable Long id, @RequestBody UserInfoUpdateRequest infoUpdateRequest){
        return new ResponseEntity<>(userService.updatePhoneNumber(id, infoUpdateRequest), HttpStatus.OK);
    }

    //github
    @Timer
    @PutMapping("user/{id}/github-account")
    @Operation(summary = "gitbub 아이디 변경", description = "해당 id 유저의 github 아이디를 변경한다.")
    public ResponseEntity<User> updateGithubAccount(@PathVariable Long id, @RequestBody UserInfoUpdateRequest infoUpdateRequest){
        return new ResponseEntity<>(userService.updateGithubId(id, infoUpdateRequest), HttpStatus.OK);
    }

    //discord
    @Timer
    @PutMapping("user/{id}/discord-account")
    @Operation(summary = "dicord 아이디 변경", description = "해당 id 유저의 discord 아이디를 변경한다.")
    public ResponseEntity<User> updateDiscordAccount(@PathVariable Long id, @RequestBody UserInfoUpdateRequest infoUpdateRequest) {
        return new ResponseEntity<>(userService.updateDiscordId(id, infoUpdateRequest), HttpStatus.OK);
    }

    // 학생정보인증
    @Timer
    @PutMapping("user/{id}/sejong-auth")
    @Operation(summary = "세종대 학생 정보 인증", description = "해당 id 유저의 세종대 학생 정보를 인증한다.\n\n 인증 되는 항목은 이름, 학번, 학년, 전공, 재학여부이다.")
    public ResponseEntity<User> updateSejongStudentAuth(@PathVariable Long id, @RequestBody SejongStudentAuthRequest sejongStudentAuthRequest){
        return new ResponseEntity<>(userService.updateSejongStudentAuth(id, sejongStudentAuthRequest), HttpStatus.OK);
    }

    @Timer
    @GetMapping("a")
    @Tag(name = "TEMP")
    @Operation(summary = "사용불가", description = "사용하지마세요")
    public ResponseEntity<AuthEmailResponse> mailSend(@RequestParam Long id, @RequestParam String email){
        return new ResponseEntity<>(signService.sendVerifyMail(id, email), HttpStatus.OK);
    }

}
