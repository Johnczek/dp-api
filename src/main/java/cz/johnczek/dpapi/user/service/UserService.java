package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.request.PatchRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;

public interface UserService {

    JwtResponse login(@NonNull String email, @NonNull String password);

    void register(@NonNull RegisterRequest registerRequest);

    void patch(long id, @NonNull PatchRequest patchRequest);
}
