package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.request.PatchRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;

public interface UserService {

    JwtResponse login(@NonNull String email, @NonNull String password);

    void register(@NonNull RegisterRequest registerRequest);

    void patch(long id, @NonNull PatchRequest patchRequest);

    Map<Long, UserDto> findByItemIds(@NonNull Set<Long> itemIds);
}
