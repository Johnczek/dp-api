package cz.johnczek.dpapi.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Schema(example = "user@user.com")
    private String email;

    @NotBlank
    @Schema(example = "user")
    private String password;
}
