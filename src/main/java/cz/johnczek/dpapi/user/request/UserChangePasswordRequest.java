package cz.johnczek.dpapi.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String password;
}
