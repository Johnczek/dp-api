package cz.johnczek.dpapi.user.controller;

import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.user.request.LoginRequest;
import cz.johnczek.dpapi.user.request.UserChangeAvatarRequest;
import cz.johnczek.dpapi.user.request.UserChangePasswordRequest;
import cz.johnczek.dpapi.user.request.UserChangeRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import cz.johnczek.dpapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<JwtResponse> login(@Validated @Valid @RequestBody LoginRequest loginRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                userService.login(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> register(@Validated @Valid @RequestBody RegisterRequest registerRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.register(registerRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "User data update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> patch(
            @PathVariable("id") long id,
            @Valid @RequestBody UserChangeRequest userChangeRequest,
            Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.patch(id, userChangeRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/avatar")
    public ResponseEntity<HttpStatus> updateUserAvatar(@PathVariable("id") long id,
                                                       @Valid @RequestBody UserChangeAvatarRequest request) {

        userService.updateUserAvatar(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/password")
    public ResponseEntity<HttpStatus> updateUserPassword(@PathVariable("id") long id,
                                                       @Valid @RequestBody UserChangePasswordRequest request) {

        userService.updateUserPassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
