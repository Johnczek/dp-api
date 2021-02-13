package cz.johnczek.dpapi.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private long id;

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private String username;

    private String email;

    private String avatarUUID;

    @Builder.Default
    private Collection<String> roles = new ArrayList<>();

}
