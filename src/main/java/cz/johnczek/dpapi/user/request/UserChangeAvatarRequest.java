package cz.johnczek.dpapi.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserChangeAvatarRequest {

    @NotBlank
    private String avatarUUID;
}
