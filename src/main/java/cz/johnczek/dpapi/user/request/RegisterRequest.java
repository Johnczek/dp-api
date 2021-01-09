package cz.johnczek.dpapi.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @NotBlank
    @Size(max = 64)
    private String password;

    @NotBlank
    @Size(max = 128)
    private String firstName;

    @NotBlank
    @Size(max = 128)
    private String lastName;

    @Size(max = 128)
    private String avatar;

    private String description;
}
