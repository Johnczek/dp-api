package cz.johnczek.dpapi.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggedUserDetails implements UserDetails {

    private final boolean accountNonExpired = true;

    private final boolean accountNonLocked = true;

    private final boolean credentialsNonExpired = true;

    private final boolean enabled = true;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    public Collection<String> getUserRoles() {

        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
