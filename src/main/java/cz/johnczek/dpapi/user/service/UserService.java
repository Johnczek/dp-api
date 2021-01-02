package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import lombok.NonNull;

public interface UserService {

    LoggedUserDetails findByEmailOrFail(@NonNull String email);
}
