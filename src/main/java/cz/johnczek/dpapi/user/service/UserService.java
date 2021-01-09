package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService{

    JwtResponse login(@NonNull String email, @NonNull String password);

    void register(@NonNull RegisterRequest registerRequest);
}
