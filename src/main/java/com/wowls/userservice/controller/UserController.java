package com.wowls.userservice.controller;

import com.wowls.userservice.dto.UserDto;
import com.wowls.userservice.vo.RequestUser;
import com.wowls.userservice.service.UserService;
import com.wowls.userservice.vo.Greeting;
import com.wowls.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UserController {

    private final Greeting greeting;
    private final UserService userService;
    private final Environment env;

    @GetMapping("/health-check")
    public String healthCheck() {
        return String.format("user service is running"
                +", port(local.server.port)="+env.getProperty("local.server.port")
                +", port(server.port)="+env.getProperty("server.port")
                +", token_secret="+env.getProperty("token.secret")
                +", token_expiration_time="+env.getProperty("token.expiration_time"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getGreetingMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        userService.createUser(userDto);
        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
