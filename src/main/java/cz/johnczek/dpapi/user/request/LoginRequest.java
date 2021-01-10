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
    @Schema(example = "admin@admin.com")
    private String email;

    @NotBlank
    @Schema(example = "admin")
    private String password;
}
