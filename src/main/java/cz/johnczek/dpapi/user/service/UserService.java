package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    LoggedUserDetails loadUserByUsername(@NonNull String email);
}
