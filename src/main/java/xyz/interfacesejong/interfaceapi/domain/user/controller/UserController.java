package xyz.interfacesejong.interfaceapi.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserDTO;
import xyz.interfacesejong.interfaceapi.domain.user.dto.UserInfoResponse;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(UserDTO userDTO){

        userService.save(userDTO.getEmail(), userDTO.getPassword());

        Map<String, String> map = new HashMap<>();
        map.put("type", HttpStatus.OK.getReasonPhrase());
        map.put("code", "200");
        map.put("message", "계정 등록에 성공.");

        return new ResponseEntity<>(map, new HttpHeaders(), HttpStatus.OK);
    }
}
