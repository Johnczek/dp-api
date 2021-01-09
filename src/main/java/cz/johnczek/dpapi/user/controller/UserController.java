package cz.johnczek.dpapi.user.controller;

import cz.johnczek.dpapi.user.request.LoginRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import cz.johnczek.dpapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(
                userService.login(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
