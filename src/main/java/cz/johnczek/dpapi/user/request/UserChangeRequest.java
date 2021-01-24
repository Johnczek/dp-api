package cz.johnczek.dpapi.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserChangeRequest {

    @NotBlank
    @Size(max = 128)
    private String firstName;

    @NotBlank
    @Size(max = 128)
    private String lastName;

    private String description;
}
