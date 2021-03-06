package cz.johnczek.dpapi.user.controller;

import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import cz.johnczek.dpapi.user.request.LoginRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.request.UserChangeAvatarRequest;
import cz.johnczek.dpapi.user.request.UserChangePasswordRequest;
import cz.johnczek.dpapi.user.request.UserChangeRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import cz.johnczek.dpapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User login endpoint")
    public ResponseEntity<JwtResponse> login(@Validated @Valid @RequestBody LoginRequest loginRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                userService.login(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User register endpoint")
    public ResponseEntity<HttpStatus> register(@Validated @Valid @RequestBody RegisterRequest registerRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.register(registerRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/logged-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Getting logged person details")
    public ResponseEntity<JwtResponse> loggedUser() {
        return new ResponseEntity<>(userService.loggedUser(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User dto retrieval")
    public ResponseEntity<UserDto> findById(@PathVariable("id") long id) {
        Optional<UserDto> userDto = userService.findById(id);

        return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
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
    @Operation(summary = "User avatar update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> updateUserAvatar(@PathVariable("id") long id,
                                                       @Valid @RequestBody UserChangeAvatarRequest request) {

        userService.updateUserAvatar(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User password update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> updateUserPassword(@PathVariable("id") long id,
                                                       @Valid @RequestBody UserChangePasswordRequest request) {

        userService.updateUserPassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/bank-account", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User bank account add endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BankAccountDto> addBankAccount(@PathVariable("userId") long userId,
                                                         @Valid @RequestBody BankAccountCreationRequest request) {

        Optional<BankAccountDto> bankAccountDto = userService.addBankAccount(userId, request);

        return bankAccountDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/{userId}/bank-account/{bankAccountId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User bank account delete endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> deleteBankAccount(@PathVariable("userId") long userId,
                                                         @PathVariable("bankAccountId") long bankAccountId) {

        userService.deleteBankAccount(bankAccountId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/address", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User address add endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AddressDto> addAddress(@PathVariable("userId") long userId,
                                                 @Valid @RequestBody AddressCreationRequest request) {

        Optional<AddressDto> addressDto = userService.addAddress(userId, request);

        return addressDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/{userId}/address/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "User address delete endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> deleteAddress(@PathVariable("userId") long userId,
                                                         @PathVariable("addressId") long addressId) {

        userService.deleteAddress(addressId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
