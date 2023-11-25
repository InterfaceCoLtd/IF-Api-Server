package xyz.interfacesejong.interfaceapi.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.subscription.service.SubscriptionService;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.dto.*;
import xyz.interfacesejong.interfaceapi.domain.user.service.SignService;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;
import xyz.interfacesejong.interfaceapi.global.jwt.TokenProvider;
import xyz.interfacesejong.interfaceapi.global.util.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final SignService signService;
    private final TokenProvider tokenProvider;
    private final SubscriptionService subscriptionService;

    @Timer
    @PostMapping()
    @Operation(summary = "신규 유저 등록", description = "신규 유저를 생성합니다.")
    public ResponseEntity<UserSignResponse> createUser(@RequestBody UserSignRequest request){
        User user = userService.saveUser(request);
        UserSignResponse response = new UserSignResponse(user);

        // 구독 레코드 생성
        subscriptionService.createSubscriptionRecord(user);

        // 인증 메일 발송
        signService.sendVerifyMail(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Timer
    @GetMapping("verify")
    public Resource verifyUserAccount(@RequestParam String code){
        if (signService.verifyUser(code)){
            return new ClassPathResource("static/verifySuccess.html");
        }
        return new ClassPathResource("static/verifyFail.html");
    }

    @Timer
    @PostMapping("auth/sign-in")
    @Operation(summary = "로그인 요청", description = "로그인 요청 기능\n\n 이메일 비밀번호는 필수\n\n 기기 Id 안 보내면 db에 등록 안 되어서 간소 로그인 불가")
    public ResponseEntity<UserSignResponse> signIn(@RequestBody UserSignRequest signInRequest){
        UserSignResponse userSignResponse = signService.signIn(signInRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", tokenProvider.generateToken(userSignResponse));
        return new ResponseEntity<>(userSignResponse, httpHeaders, HttpStatus.OK);
    }

    @Timer
    @PostMapping("auth/simple-sign-in")
    @Operation(summary = "간소 로그인 요청", description = "간소 로그인, 토큰 발급용 기능\n\n deviceId만 전달하면 토큰 발급")
    public ResponseEntity<UserSignResponse> simpleSignIn(@RequestBody UserSignRequest signRequest){
        UserSignResponse userSignResponse = signService.simpleSignIn(signRequest);
        String token = tokenProvider.generateToken(userSignResponse);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        return new ResponseEntity<>(userSignResponse, httpHeaders, HttpStatus.OK);

    }

    @Timer
    @GetMapping()
    @Operation(summary = "전체 유저 조회", description = "모든 유저를 조회합니다.")
    public ResponseEntity<List<UserInfoResponse>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Timer
    @DeleteMapping("user/{id}/email/{email}")
    @Operation(summary = "계정 삭제", description = "해당하는 id와 email이 일치하는 계정 정보를 delete 상태로 변경후, 모든 데이터를 null로 변경합니다.\n\n pk와 레코드는 그대로 남아있습니다.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @PathVariable String email){
        userService.deleteUser(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Timer
    @GetMapping("exists")
    @Operation(summary = "이메일 중복 검사", description = "해당 이메일이 db에 존재하는 계정인지 확인합니다.")
    public ResponseEntity<BaseResponse> checkEmailDuplication(@RequestParam String email){
        return new ResponseEntity<>(userService.hasEmail(email), HttpStatus.OK);
    }

    //비밀번호
    @Timer
    @PutMapping("user/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "해당 id 유저의 비밀번호를 변경한다.")
    public ResponseEntity<User> generateNewPassword(@PathVariable Long id, @RequestBody UserNewPasswordRequest newPasswordRequest){
        return new ResponseEntity<>(userService.resetPassword(id, newPasswordRequest), HttpStatus.OK);
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

    // 기기정보
    @Timer
    @PutMapping("user/{id}/device-id")
    @Operation(summary = "기기정보 변경", description = "해당 id 유저의 device 아이디를 변경한다. device 아이디는 UUID 형식이다.")
    public ResponseEntity<User> updateDeviceId(@PathVariable Long id, @RequestBody UserInfoUpdateRequest infoUpdateRequest){
        return new ResponseEntity<>(userService.updateDeviceId(id, infoUpdateRequest), HttpStatus.OK);
    }

    // 학생정보인증
    @Timer
    @PutMapping("user/{id}/sejong-auth")
    @Operation(summary = "세종대 학생 정보 인증", description = "해당 id 유저의 세종대 학생 정보를 인증한다.\n\n 인증 되는 항목은 이름, 학번, 학년, 전공, 재학여부이다.")
    public ResponseEntity<User> updateSejongStudentAuth(@PathVariable Long id, @RequestBody SejongStudentAuthRequest sejongStudentAuthRequest){
        return new ResponseEntity<>(userService.updateSejongStudentAuth(id, sejongStudentAuthRequest), HttpStatus.OK);
    }


}
