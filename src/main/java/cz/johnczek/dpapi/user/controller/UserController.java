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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Method login user")
    @ApiResponse(responseCode = "200", description = "Dto holding logged user details")
    @ApiResponse(responseCode = "400", description = "In case that request is valid")
    @ApiResponse(responseCode = "403", description = "In case of bad credentials or when user was not found")
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JwtResponse> login(@Parameter(description = "Request holding login credentials") @Validated @Valid @RequestBody LoginRequest loginRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                userService.login(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @Operation(summary = "Method registers user")
    @ApiResponse(responseCode = "201", description = "In case that registration was successful")
    @ApiResponse(responseCode = "400", description = "In case that request is not valid")
    @PostMapping(value = "/register", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> register(@Parameter(description = "Request holding new user data") @Validated @Valid @RequestBody RegisterRequest registerRequest, Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.register(registerRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Method retrieves login user detail")
    @ApiResponse(responseCode = "200", description = "Dto holding logged user details")
    @ApiResponse(responseCode = "404", description = "In case there is no logged user")
    @PostMapping(value = "/logged-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<JwtResponse> loggedUser() {
        return new ResponseEntity<>(userService.loggedUser(), HttpStatus.OK);
    }

    @Operation(summary = "Method finds user by id")
    @ApiResponse(responseCode = "200", description = "Dto holding user details")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserDto> findById(@Parameter(description = "Id of user we want to find") @PathVariable("id") long id) {
        Optional<UserDto> userDto = userService.findById(id);

        return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Method updates user base data", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "If update was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> patch(
            @Parameter(description = "Id of user we want to update") @PathVariable("id") long id,
            @Parameter(description = "New user data") @Valid @RequestBody UserChangeRequest userChangeRequest,
            Errors errors) {

        if (errors.hasErrors()) {
            // TODO Complete validation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.patch(id, userChangeRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Method updates users avatar", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "If update was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user or image was not found")
    @PatchMapping(value = "/{id}/avatar")
    public ResponseEntity<HttpStatus> updateUserAvatar(@Parameter(description = "Id of user we want to update") @PathVariable("id") long id,
                                                       @Parameter(description = "New picture identifier") @Valid @RequestBody UserChangeAvatarRequest request) {

        userService.updateUserAvatar(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Method updates user password", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "If update was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @PatchMapping(value = "/{id}/password", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> updateUserPassword(@Parameter(description = "Id of user we want to update") @PathVariable("id") long id,
                                                         @Parameter(description = "New password") @Valid @RequestBody UserChangePasswordRequest request) {

        userService.updateUserPassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Method adds bank account to user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "If creation was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @PostMapping(value = "/{userId}/bank-account", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BankAccountDto> addBankAccount(@Parameter(description = "Id of user for which we want to add new bank account") @PathVariable("userId") long userId,
                                                         @Parameter(description = "Data of new bank account") @Valid @RequestBody BankAccountCreationRequest request) {

        Optional<BankAccountDto> bankAccountDto = userService.addBankAccount(userId, request);

        return bankAccountDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Method deletes bank account by given id from user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "If deletion was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @DeleteMapping(value = "/{userId}/bank-account/{bankAccountId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> deleteBankAccount(@Parameter(description = "Id of user from which we want to delete bank account") @PathVariable("userId") long userId,
                                                        @Parameter(description = "Id of bank account we want to delete") @PathVariable("bankAccountId") long bankAccountId) {

        userService.deleteBankAccount(bankAccountId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Method adds address to user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "If addition  was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @PostMapping(value = "/{userId}/address", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressDto> addAddress(@Parameter(description = "Id of user for which we want to add address") @PathVariable("userId") long userId,
                                                 @Parameter(description = "Data of new address") @Valid @RequestBody AddressCreationRequest request) {

        Optional<AddressDto> addressDto = userService.addAddress(userId, request);

        return addressDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Method deletes address from user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "If deletion was successful")
    @ApiResponse(responseCode = "400", description = "In case data are not valid")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to update user with given id")
    @ApiResponse(responseCode = "404", description = "In case that user was not found")
    @DeleteMapping(value = "/{userId}/address/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> deleteAddress(@Parameter(description = "Id of user from which we want to delete address") @PathVariable("userId") long userId,
                                                    @Parameter(description = "Id of address we want to delete") @PathVariable("addressId") long addressId) {

        userService.deleteAddress(addressId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
